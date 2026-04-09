import request from '@/utils/request'

// 查询报销单据主列表
export function listDoc(query) {
  return request({
    url: '/reimburseDoc/list',
    method: 'get',
    params: query
  })
}

export function listSummary(query) {
  return request({
    url: '/reimburseDoc/listSummary',
    method: 'get',
    params: query
  })
}

// 查询报销单据主详细
export function getDoc(docId) {
  return request({
    url: '/reimburseDoc/' + docId,
    method: 'get'
  })
}

// 新增报销单据主
export function addDoc(data) {
  return request({
    url: '/reimburseDoc',
    method: 'post',
    data: data
  })
}

// 修改报销单据主
export function updateDoc(data) {
  return request({
    url: '/reimburseDoc',
    method: 'put',
    data: data
  })
}

// 删除报销单据主
export function delDoc(docId) {
  return request({
    url: '/reimburseDoc/' + docId,
    method: 'delete'
  })
}

// 提交审批
export function submitReimburse(docId) {
  return request({
    url: '/reimburseDoc/submit/' + docId,
    method: 'post'
  })
}

// 撤销申请
export function cancelReimburse(docId) {
  return request({
    url: '/reimburseDoc/cancel/' + docId,
    method: 'post'
  })
}