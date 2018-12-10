try {
    Alpaca.Tpl({template: './header.html', to: '#header'});
    Alpaca.Tpl({template: './middle-left.html', to: '#middle-left'});
} catch (e) {
    console.log(e);
}

$().ready(function () {
    Alpaca.run("#/auth/index/index");
    let topMenuId = Alpaca.Router.getParams("topMenuId");
    if (!isEmpty(topMenuId)) {
        sessionStorage.setItem("topMenuId", topMenuId);
    }
});