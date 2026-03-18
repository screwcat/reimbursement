import request from '@/utils/request'

// 查询报销申请单主列表
export function listReimburse(query) {
  return request({
    url: '/reimburse/list',
    method: 'get',
    params: query
  })
}

// 查询报销申请单主详细
export function getReimburse(reimburseId) {
  return request({
    url: '/reimburse/' + reimburseId,
    method: 'get'
  })
}

// 新增报销申请单主
export function addReimburse(data) {
  return request({
    url: '/reimburse',
    method: 'post',
    data: data
  })
}

// 修改报销申请单主
export function updateReimburse(data) {
  return request({
    url: '/reimburse',
    method: 'put',
    data: data
  })
}

// 删除报销申请单主
export function delReimburse(reimburseId) {
  return request({
    url: '/reimburse/' + reimburseId,
    method: 'delete'
  })
}
