import request from '@/utils/request'

// 查询报销单据主列表
export function listDoc(query) {
  return request({
    url: '/system/doc/list',
    method: 'get',
    params: query
  })
}

// 查询报销单据主详细
export function getDoc(docId) {
  return request({
    url: '/system/doc/' + docId,
    method: 'get'
  })
}

// 新增报销单据主
export function addDoc(data) {
  return request({
    url: '/system/doc',
    method: 'post',
    data: data
  })
}

// 修改报销单据主
export function updateDoc(data) {
  return request({
    url: '/system/doc',
    method: 'put',
    data: data
  })
}

// 删除报销单据主
export function delDoc(docId) {
  return request({
    url: '/system/doc/' + docId,
    method: 'delete'
  })
}
