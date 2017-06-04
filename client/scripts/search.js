var $ = require("jquery");

var toAjax = function(url) {
    return url + "&ajax=true";
};

var paginationHandler = function() {
    $(".btn-pagination").click(function() {
        var url = $(this).attr("href");
        updatePage(url);
        return false;
    });
};

var updatePage = function(url) {
    $.get(toAjax(url), function(data, status) {
        if(status === "success") {
            $(results).empty();
            $(results).append($(data));
            paginationHandler();
        }
    });
};

$(document).ready(function() {
    var results = $("#results");

    $("#searchForm").submit(function() {
        var endpoint = $(this).attr("action");
        var searchValue = $(this).find("[name='q']").val();

        if(searchValue) {
            var url = endpoint + "?q=" + encodeURI(searchValue);
            updatePage(url);
        }

        return false;
    });

    paginationHandler();
});
