$(function () {
  $('input[name="gender"][value="M"]').prop('checked', true);
  $('.range-slider').jRange({
    from: 0,
    to: 100,
    step: 1,
    scale: [0, 25, 50, 75, 100],
    format: '%s',
    width: 300,
    showLabels: true,
    isRange: true
  });
  $('.range-slider').jRange('setValue', '18,30');
  $('.slider').jRange('updateRange', '18,150');

  $(document).ready(function () {
    $('#btnGet').click(function () {
      checked = $("input[type=checkbox]:checked").length;
      if (!checked) {
        alert("請至少選擇一項技能");
        return false;
      }
    });
  });
})


