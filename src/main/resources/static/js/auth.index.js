Alpaca.AuthModule = {};

Alpaca.AuthModule.IndexController = {

    indexAction: function () {
    },

    userListAction: function () {
        $("#layui-body").load("./auth/user/userList.html", function () {
            renderAll();
        });
    },

    userList2Action: function () {
        $("#layui-body").load("./auth/user/userList.html", function () {
            renderAll();
        });
    }
};