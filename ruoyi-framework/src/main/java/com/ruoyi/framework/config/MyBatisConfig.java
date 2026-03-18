package com.ruoyi.framework.config;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;

import javax.sql.DataSource;

/**
 * 若依原生 MyBatis 配置 + MP 插件兼容
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.ruoyi.**.mapper"}) // 扫描所有 Mapper
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;

    // 注入 MP 分页插件（关键：将 MP 插件加入原生 SqlSessionFactory）
    @Autowired
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        // 1. 若依原生 SqlSessionFactoryBean（核心：保留原生）
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 2. 若依原生 XML 扫描（确保 SysJobMapper.xml 被加载）
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath:mapper/**/*.xml"));
        bean.setTypeAliasesPackage("com.ruoyi.**.domain");

        // 3. 若依原生配置（下划线转驼峰）
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);

        // 4. 若依原生 VFS 配置
        VFS.addImplClass(SpringBootVFS.class);

        // 5. 添加 MP 分页插件（兼容 MP 功能）
        bean.setPlugins(mybatisPlusInterceptor);

        return bean.getObject();
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
