/**
 * @author xiangqian
 * @date 10:48 2024/03/10
 */

;
$(function () {
    // jvm
    let $jvmVendor = $('#jvmVendor')
    let $jvmVersion = $('#jvmVersion')
    let $jvmMaxMemory = $('#jvmMaxMemory')
    let $jvmFreeMemory = $('#jvmFreeMemory')

    // os
    let $osName = $('#osName')
    let $osCpuLoad = $('#osCpuLoad')
    let $osTotalMemory = $('#osTotalMemory')
    let $osFreeMemory = $('#osFreeMemory')

    // SSE事件流的URL
    const eventSource = new EventSource('/sse/jvm,os')

    // 在连接建立时
    eventSource.onopen = function (event) {
        // console.log('连接已建立')
    }

    // 在发生错误时
    eventSource.onerror = function (event) {
        console.error('发生错误', event)
    }

    // 在接收到新的事件时
    eventSource.onmessage = function (event) {
        // console.log('接收到事件', event.data)
    }

    eventSource.addEventListener('jvm', function (event) {
        const data = JSON.parse(event.data)
        // console.log('接收到jvm事件', data)
        $jvmVendor.text(data.vendor)
        $jvmVersion.text(data.version)
        $jvmMaxMemory.text(data.maxMemory)
        $jvmFreeMemory.text(data.freeMemory)
    })

    eventSource.addEventListener('os', function (event) {
        const data = JSON.parse(event.data)
        // console.log('接收到os事件', data)
        $osName.text(data.name)
        $osCpuLoad.text(data.cpuLoad)
        $osTotalMemory.text(data.totalMemory)
        $osFreeMemory.text(data.freeMemory)
    })
})
;