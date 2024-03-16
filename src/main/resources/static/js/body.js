/**
 * @author xiangqian
 * @date 22:21 2024/03/02
 */

;
$(function () {
    // 鼠标进入<tr>时执行的函数
    $('body > table tbody tr').mouseenter(function (event) {
        // 获取目标<tr>元素
        let $tr = $(event.currentTarget)

        // 获取目标<tr>元素中的最后一个<td>元素
        let $lastTd = $($tr.find('td:last'))

        // 显示<td>元素
        $lastTd.css('visibility', 'visible')
    });

    // 鼠标离开<tr>时执行的函数
    $('body > table tbody tr').mouseleave(function (event) {
        // 获取目标<tr>元素
        let $tr = $(event.currentTarget)

        // 获取目标<tr>元素中的最后一个<td>元素
        let $lastTd = $($tr.find('td:last'))

        // 隐藏<td>元素
        $lastTd.css('visibility', 'hidden')
    });

})
;
