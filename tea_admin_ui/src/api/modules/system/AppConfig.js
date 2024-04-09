import request from '@/utils/axiosRequest.js'


// 获取小程序的所有配置信息
export function getAppConfig(){
  return request({
    url: "/config" ,
    method: "get"
  })
}


// 修改配置信息
export function updateAppConfig(appConfig) {
  return request({
    url: "/config",
    method: "put",
    data: appConfig
  })
}

// 获取首页报表数据
export function getStatData(){
  return request({
    url: "/orderInfoAdmin/statData" ,
    method: "get"
  })
}