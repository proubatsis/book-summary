var $ = require("jquery");
var RESULTS = "#results";

var historyPush = history.pushState;

var getState = function() {
    return $(RESULTS).html();
};

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
    $(".modal").addClass("loading");
    $.get(toAjax(url), function(data, status) {
        $(".modal").removeClass("loading");
        if(status === "success") {
            historyPush(data, null, url);

            $(RESULTS).empty();
            $(RESULTS).append($(data));
            paginationHandler();
        }
    });
};

$(document).ready(function() {
    historyPush(getState(), null, location.path);

    $("#searchForm").submit(function() {
        var endpoint = $(this).attr("action");
        var searchValue = $(this).find("[name='q']").val();

        if(searchValue) {
            var url = endpoint + "?q=" + encodeURI(searchValue);
            updatePage(url);
        }

        return false;
    });

    $(window).on("popstate", function(e) {
        var state = e.originalEvent.state;
        $(RESULTS).empty();
        $(RESULTS).append($(state));
        paginationHandler();
    });

    paginationHandler();
});
