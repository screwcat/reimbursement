import request from '@/utils/request'

// 查询报销申请列表
export function listReimburse(query) {
  return request({
    url: '/reimburse/list',
    method: 'get',
    params: query
  })
}

export function listBydocId(query) {
  return request({
    url: '/reimburse/listBydocId',
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

// 新增报销申请单
export function addReimComplete(data) {
  return request({
    url: '/reimburse/addReimburse',
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

// 上传票据影像
export function uploadAttachment(file) {
  return request({
    url: '/reimburse/upload',
    method: 'post',
    data: file
  })
}
export function changeProcessState(data) {
  return request({
    url: '/remiburseDoc/changeProcessState',
    method: 'post',
    data: data
  })
}

export function getTravelStatistics(query) {
  return request({
    url: '/reimburse/travelStatistics',
    method: 'get',
    params: query
  });
}