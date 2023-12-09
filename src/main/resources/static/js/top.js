$.widget.bridge('uitooltip', $.ui.tooltip);
$(function () {
    $('[data-bs-toggle="tooltip"]').tooltip();
});