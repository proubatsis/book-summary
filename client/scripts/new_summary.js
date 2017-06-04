var ko = require("knockout");
var markdown = require("markdown").markdown;
var $ = require("jquery");

var EDITOR = "#summaryEditor";
var PREVIEW = "#summaryPreview";

// Update preview
var NewSummViewModel = function() {
    this.summaryArea = ko.observable();

    this.rendered = ko.computed(function() {
        var summary = this.summaryArea();
        return markdown.toHTML(summary ? summary : "");
    }, this);
};

ko.applyBindings(new NewSummViewModel());

// Make preview same height as summary
var summaryHeight = Math.floor($(EDITOR).height());
$(PREVIEW).css("max-height", Math.floor(summaryHeight) + "px");

// Display scroll bar
$(EDITOR).on("keyup", function() {
    var previewHeight = Math.floor($(PREVIEW).height());
    if(previewHeight >= (summaryHeight / 1.25))
        $(PREVIEW).css("overflow-y", "scroll");
});
