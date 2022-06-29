$(function() {
 //	$('input[name="gender"][value="M"]').prop('checked', true);

	$(document).ready(function() {
		$(function() {
			$.datepicker.setDefaults($.datepicker.regional["zh-TW"]);
			$("#date_picker").datepicker({
				numberofMonths: 1,
				changeYear: true,
				changeMonth: true,
				showOtherMonths: true,
				yearRange: "-100:-18",
				minDate: new Date(1900, 1, 1),
				maxDate: '-18Y',
				dateFormat: "yy/mm/dd"
			});
		});

		$('#btnGet').click(function() {
			var format = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
			var temp = document.getElementById("userName").value;
			checked = $("input[type=checkbox]:checked").length;

			if (document.getElementById("userName").value.length == 0) {
				alert("請輸入您的暱稱");
			} else if (format.test(temp)) {
				alert("請勿輸入特殊字元");
			} else if (!checked) {
				alert("請至少選擇一項技能");
			}
		});
	});
});


