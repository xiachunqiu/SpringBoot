layui.use(['element', 'jquery', 'layer', 'laypage', 'table'], function () {
    let $ = layui.jquery;
    let laypage = layui.laypage;
    let table = layui.table;

    getList();

    $("#searchFormBtn").on("click", function () {
        $("#pageNo").val(1);
        getList();
        return false;
    });

    $("#clearFormBtn").on("click", function () {
        $("#searchForm").resetForm();
        return false;
    });

    function getList() {
        $.get('/user/getUserList', $('#searchForm').serialize(), function (rtn) {
            table.render({
                id: "dataListTable",
                elem: '#dataList',
                data: rtn.data.list,
                loading: true,
                cols: [[
                    {field: 'id', title: 'ID', width: '10%'},
                    {field: 'userName', title: 'user name', width: '20%'},
                    {field: 'addDateStr', title: 'Add date', width: '20%'},
                    {field: 'sex', title: 'Sex', width: '10%'},
                    {field: 'address', title: 'Address', width: '20%'},
                    {fixed: 'right', width: '15%', align: 'center', toolbar: '#operateTpl'}
                ]], skin: 'row',
                even: true,
                page: false
            });
            /** @namespace rtn.pagerDTO.recordCount */
            /** @namespace rtn.pagerDTO.pageSize */
            /** @namespace rtn.pagerDTO.pageNo */
            laypage.render({
                elem: 'pager',
                count: rtn.data.pagerDTO.recordCount,
                limit: rtn.data.pagerDTO.pageSize,
                curr: rtn.data.pagerDTO.pageNo,
                prev: 'prev',
                next: 'next',
                jump: function (obj, first) {
                    $("#pageNo").val(obj.curr);
                    if (!first) {
                        getList();
                    }
                },
                layout: ['prev', 'page', 'next', 'skip', 'count']
            });
        });
    }

    // tool
    table.on('tool(dataList)', function (obj) {
        let data = obj.data;
        if (obj.event === 'detail') { // detail
            let $showDetailTable = $("#showDetailTable");
            /** @namespace data.addDateStr */
            /** @namespace data.sex */
            $showDetailTable.empty()
                .append("<tr><td width='80'>ID</td><td width='150'>" + data.id + "</td></tr>")
                .append("<tr><td width='80'>user name</td><td width='150'>" + data.userName + "</td></tr>")
                .append("<tr><td width='80'>Add date</td><td width='150'>" + data.addDateStr + "</td></tr>")
                .append("<tr><td width='80'>Sex</td><td width='150'>" + data.sex + "</td></tr>")
                .append("<tr><td width='80'>Address</td><td width='150'>" + data.address + "</td></tr>");
            layer.open({
                type: 1,
                title: "detail",
                skin: 'layui-layer-rim',
                area: ['600px', '320px'],
                content: $("#showDetailDiv").html()
            });
        } else if (obj.event === 'edit') { // edit
            addOrEditUser(false);
            $("#addUser").initForm({jsonValue: data});
            renderAll();
        }
    });

    // add
    $("#addSpan").on("click", function () {
        addOrEditUser(true);
        renderAll();
    });

    function addOrEditUser(isAdd) {
        $.ajax({
            type: "get",
            url: '/page/auth/user/addOrEditUser.html',
            data: null,
            async: false,
            dataType: 'html',
            success: function (data) {
                layer.open({
                    type: 1,
                    title: 'Add user',
                    area: ['500px', '300px'],
                    anim: 4,
                    skin: 'layui-layer-molv',
                    btn: ['save'],
                    yes: function (index) {
                        let url = isAdd ? '/user/addUser' : '/user/updateUser';
                        $.post(url, $('#addForm').serialize(), function (rtn) {
                            if (rtn.success) {
                                layer.close(index);
                                getList();
                                layer.confirm(rtn.msg, {
                                    btn: ['OK']
                                }, function (index) {
                                    layer.close(index);
                                });
                            } else {
                                layer.msg(rtn.msg);
                            }
                        });
                    },
                    cancel: function (index) {
                        layer.close(index);
                    },
                    content: data
                });
            }
        });
    }
});