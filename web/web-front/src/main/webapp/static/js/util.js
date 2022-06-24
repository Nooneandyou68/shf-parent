var util = {
    getQueryVariable(variable) {
        //  http://localhost:8001/info.html?id=3&name=z3
        var query = window.location.search.substring(1);  // id=3&name=z3
        var vars = query.split("&"); //['id=3','name=z3']
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("="); //['id',3]    ['name','z3']
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    },

    getOriginUrl() {
        var query = window.location.search.substring(1);
        if(query.indexOf("originUrl") != -1) {
            var vars = query.split("originUrl=");
            return vars[1];
        }
        return ""
    }
}