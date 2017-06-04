var ko = require("knockout");
var markdown = require("markdown").markdown;
var $ = require("jquery");

var EDITOR = "#summaryEditor";
var PREVIEW = "#summaryPreview";

var DEFAULT_MESSAGE = "# Start typing\n\n";
DEFAULT_MESSAGE += "This is where your summary preview will be displayed. Formatting is done via **markdown**.\n\n";
DEFAULT_MESSAGE += "You do not need to use markdown, but if you would like to add formatting to your summary then it will be helpful to learn the basics."
DEFAULT_MESSAGE += "For example, you can make lists, or use bold and italics.\n\n";
DEFAULT_MESSAGE += "- Plot point 1\n";
DEFAULT_MESSAGE += "- **Plot** point 2\n";
DEFAULT_MESSAGE += "- *Plot* point 3\n";
DEFAULT_MESSAGE += "## How the above message was formatted\n\n";
DEFAULT_MESSAGE += "\`# Start typing\`\n\n";
DEFAULT_MESSAGE += "\`This is where your summary preview will be displayed. Formatting is done via **markdown**.\`\n\n";
DEFAULT_MESSAGE += "\`You do not need to use markdown, but if you would like to add formatting to your summary then it will be helpful to learn the basics.\`\n\n"
DEFAULT_MESSAGE += "\`For example, you can make lists, or use bold and italics.\`\n\n";
DEFAULT_MESSAGE += "\`- Plot point 1\`\n\n";
DEFAULT_MESSAGE += "\`- **Plot** point 2\`\n\n";
DEFAULT_MESSAGE += "\`- *Plot* point 3\`\n\n";

// Update preview
var NewSummViewModel = function() {
    this.summaryArea = ko.observable();

    this.rendered = ko.computed(function() {
        var summary = this.summaryArea();
        return markdown.toHTML(summary ? summary : DEFAULT_MESSAGE);
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
