import request from '@/utils/request'

// 查询报销申请列表
export function listReimburse(query) {
  return request({
    url: '/reimburse/list',
    method: 'get',
    params: query
  })
}

// 查询报销申请详细
export function getReimburse(reimburseId) {
  return request({
    url: '/reimburse/' + reimburseId,
    method: 'get'
  })
}

// 新增报销申请
export function addReimburse(data) {
  return request({
    url: '/reimburse',
    method: 'post',
    data: data
  })
}

// 修改报销申请
export function updateReimburse(data) {
  return request({
    url: '/reimburse',
    method: 'put',
    data: data
  })
}

// 删除报销申请
export function delReimburse(reimburseIds) {
  return request({
    url: '/reimburse/' + reimburseIds,
    method: 'delete'
  })
}

// 提交审批
export function submitReimburse(reimburseId) {
  return request({
    url: '/reimburse/submit/' + reimburseId,
    method: 'post'
  })
}

// 撤销申请
export function cancelReimburse(reimburseId) {
  return request({
    url: '/reimburse/cancel/' + reimburseId,
    method: 'post'
  })
}

// 上传票据影像
export function uploadAttachment(file) {
  return request({
    url: '/reimburse/upload',
    method: 'post',
    data: file
  })
}