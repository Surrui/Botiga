jQuery(document).ready(
		function() {

			$(".editBtn").click(
					function(e) {

						e.preventDefault();

						var nombre = $(this).closest("tr").children(
								":nth-child(1)").text();
						var precio = $(this).closest("tr").children(
								":nth-child(8)").text();
						var genero = $(this).closest("tr").children(
								":nth-child(2)").text();
						var distribuidora = $(this).closest("tr").children(
								":nth-child(3)").text();
						var plataforma = $(this).closest("tr").children(
								":nth-child(4)").text();
						var edad = $(this).closest("tr").children(
								":nth-child(5)").text();
						var cantidad = $(this).closest("tr").children(
								":nth-child(6)").text();
						var activar = $(this).closest("tr").children(
								":nth-child(7)").text();
						var id = $(this).closest("tr").data("id");

						$("#nom_juego").val(nombre);
						$("#pre_juego").val(precio);
						$("#gen_juego").val(genero);
						$("#dist_juego").val(distribuidora);
						$("#plat_juego").val(plataforma);
						$("#edad_rec").val(edad);
						$("#cant_juego").val(cantidad);
						$("#activar_juego").val(activar);
						$("#hidden_id").val(id);

					});

			setTimeout(errorNumber, 5000);
			setTimeout(errorCSV, 5000);
			setTimeout(errorActive, 5000);
			setTimeout(errorImg, 5000);
			setTimeout(errorProduct, 5000);
			setTimeout(errorType, 5000);
			setTimeout(errorEdit, 5000);

			function errorCSV() {
				$("#err_csv").slideUp();
			}
			function errorNumber() {
				$("#err_number").slideUp();
			}
			function errorActive() {
				$("#err_active").slideUp();
			}
			function errorImg() {
				$("#err_img").slideUp();
			}
			function errorProduct() {
				$("#err_product").slideUp();
			}
			function errorType() {
				$("#err_type").slideUp();
			}
			function errorEdit() {
				$("#err_edit").slideUp();
			}

			$(".editImg").on("click", function(e) {

				var src = $(this).attr("src");

				$("#img_prod").attr("src", src);
				$("#img_prod").attr("big", src);
				$(".zoomer-wrapper-big img").attr("src", src);

			});
		});