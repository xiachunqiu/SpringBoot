$().ready(function () {
    getTopMenus();
    getLeftMenus();
    $("#userInfo").html("Hugh");

    function getTopMenus() {
        let topMenuId = sessionStorage.getItem("topMenuId");
        if (isEmpty(topMenuId)) {
            topMenuId = "";
        }
        $.ajax({
            type: 'get',
            url: "/sysManager/getTopMenus",
            data: null,
            success: function (rtn) {
                if (rtn.code === "200") {
                    $.each(rtn.data, function (i, n) {
                        if (isEmpty(n.childList)) {
                            /** @namespace n.iconType */
                            $("#topMenuHeader").append("<li class=\"layui-nav-item\"><a href=\"javascript:void(0);\" id='top-" + n.id + "'><i class=\"" + n.iconType + "\">" + n.icon + "</i>&nbsp;&nbsp;" + n.name + "</a></li>");
                        } else {
                            $("#topMenuHeader").append("<li class=\"layui-nav-item\"><a href=\"javascript:void(0);\" id='top-" + n.id + "'><i class=\"" + n.iconType + "\">" + n.icon + "</i>&nbsp;&nbsp;" + n.name + "</a><dl class=\"layui-nav-child\" id=\"layui-nav-child" + i + "\"></dl></li>");
                            $.each(n.childList, function (ii, nn) {
                                $("#layui-nav-child" + i).append("<dd><a href=\"" + "javascript:void(0);" + "\" id='top-" + nn.id + "'><i class=\"" + nn.iconType + "\">" + nn.icon + "</i>&nbsp;&nbsp;" + nn.name + "</a></dd>");
                            });
                        }
                    });
                    $("#topMenuHeader").addClass("layui-nav layui-layout-left");
                    $("#top-" + topMenuId).parent("li").addClass("layui-this");
                    renderAll();
                    element.on('nav(topMenuHeader)', function (elem) {
                        clearLocationUrl();
                        let topMenuId = elem.attr("id");
                        topMenuId = topMenuId.split("-")[1];
                        if (!isEmpty(topMenuId)) {
                            sessionStorage.setItem("topMenuId", topMenuId);
                        }
                        elem.parent("dd").addClass("layui-this");
                        getLeftMenus("top");
                    });
                }
            },
            dataType: "json",
            error: function (data) {
                layer.msg(data);
            }
        });
    }

    function clearLocationUrl() {
        let stateObject = {};
        let url = window.location.href;
        url = url.split(".html")[0] + ".html";
        history.pushState(stateObject, document.title, url);
    }

    function getLeftMenus() {
        let topMenuId = sessionStorage.getItem("topMenuId");
        let leftMenuId = sessionStorage.getItem("leftMenuId");
        if (isEmpty(topMenuId)) {
            topMenuId = "1";
        }
        if (isEmpty(leftMenuId)) {
            leftMenuId = "";
        }
        let leftMenuIdEqualIn = false;
        let openHashUrl = "#/auth/index/index";
        let inChildList = false;
        $.ajax({
            type: 'get',
            url: "/sysManager/getLeftMenus",
            data: {topMenuId: topMenuId},
            success: function (rtn) {
                if (rtn.code === "200") {
                    let $leftMenuMiddle = $("#leftMenuMiddle");
                    $leftMenuMiddle.empty();
                    let firstMenuId = "";
                    $.each(rtn.data, function (i, n) {
                        if (isEmpty(firstMenuId) && n.childList.length === 0) {
                            firstMenuId = n.id;
                            openHashUrl = n.url;
                            if (isEmpty(leftMenuId)) {
                                sessionStorage.setItem("leftMenuId", firstMenuId);
                                leftMenuId = firstMenuId;
                            }
                        }
                        if (leftMenuId === n.id) {
                            leftMenuIdEqualIn = true;
                            openHashUrl = n.url;
                        }
                        if (isEmpty(n.childList)) {
                            $leftMenuMiddle.append(
                                "<li class=\"layui-nav-item\" id='extend" + n.id + "'>"
                                + "<a id='left-" + n.id + "' href=\"" + n.url + "?leftMenuId=" + n.id + "&topMenuId=" + n.topMenuId + "\"" + "\">"
                                + "<i class=\"" + n.iconType + "\">" + n.icon + "</i>&nbsp;&nbsp;" + n.name
                                + "</a></li>");
                        } else {
                            $leftMenuMiddle.append(
                                "<li class=\"layui-nav-item\" id='extend" + n.id + "'>"
                                + "<a id='left-" + n.id + "' href=\"javascript:void(0);\">"
                                + "<i class=\"" + n.iconType + "\">" + n.icon + "</i>&nbsp;&nbsp;" + n.name + "</a>"
                                + "<dl class=\"layui-nav-child\" id=\"layui-nav-child-left" + i + "\"></dl></li>");
                            $.each(n.childList, function (ii, nn) {
                                if (isEmpty(firstMenuId)) {
                                    firstMenuId = nn.id;
                                    openHashUrl = nn.url;
                                    if (isEmpty(leftMenuId)) {
                                        sessionStorage.setItem("leftMenuId", firstMenuId);
                                        leftMenuId = firstMenuId;
                                    }
                                }
                                if (!inChildList && !leftMenuIdEqualIn) {
                                    openHashUrl = nn.url;
                                    inChildList = true;
                                    leftMenuIdEqualIn = true;
                                }
                                $("#layui-nav-child-left" + i).append(
                                    "<dd id='left-dd" + nn.id + "'>"
                                    + "<a id='left-" + nn.id + "' href=\"" + nn.url + "?leftMenuId=" + nn.id + "&topNavId=" + nn.topMenuId + "\">"
                                    + "<i class=\"" + nn.iconType + "\">" + nn.icon + "</i>&nbsp;&nbsp;" + nn.name + "</a></dd>");
                            });
                        }
                    });
                    $leftMenuMiddle.addClass("layui-nav layui-nav-tree");
                    let $left_leftMenuId = $("#left-" + leftMenuId);
                    $left_leftMenuId.parents("li").addClass("layui-nav-itemed");
                    $left_leftMenuId.addClass("layui-this");
                    if (isEmpty(leftMenuId) || !leftMenuIdEqualIn) {
                        $("#extend" + firstMenuId).addClass("layui-nav-itemed");
                    }
                    Alpaca.run(openHashUrl);
                    renderAll();
                    element.on('nav(left-nav)', function (elem) {
                        let leftId = elem.attr("id");
                        leftId = leftId.split("-")[1];
                        if (!isEmpty(leftId)) {
                            sessionStorage.setItem("leftMenuId", leftId);
                        }
                        elem.parent("dd").addClass("layui-this");
                    });
                }
            },
            dataType: "json",
            error: function (data) {
                layer.msg(data);
            }
        });
    }

    $('#modifyPassword').on("click", function () {
        $.ajax({
            type: "get",
            url: '/page/modifyPassword.html',
            data: null,
            async: false,
            dataType: 'html',
            success: function (data) {
                layer.open({
                    type: 1,
                    title: "Modify password",
                    skin: 'layui-layer-rim',
                    area: ['600px', '350px'],
                    content: data
                });
            }
        });
    });

    form.on('submit(modifyPasswordSubmit)', function () {
        let newPassword = $("#newPassword").val();
        let newPasswordConfirm = $("#newPasswordConfirm").val();
        if (newPassword !== newPasswordConfirm) {
            layer.msg("Two inconsistent new passwords");
            return false;
        }
        $.post('/sysManager/changePassword', $('#modifyPasswordForm').serialize(), function (rtn) {
            layer.msg(rtn.msg);
        });
        return false;
    });
});