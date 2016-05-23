package net.javierjimenez.Controllers;

/*import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;*/
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
/*import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;*/

import net.javierjimenez.Models.Carrito;
import net.javierjimenez.Models.Producte;
import net.javierjimenez.Models.Sell;
import net.javierjimenez.Models.Usuari;
import net.javierjimenez.Repositories.CarritoRepositori;
import net.javierjimenez.Repositories.ProducteRepositori;
import net.javierjimenez.Repositories.ProducteService;
import net.javierjimenez.Repositories.UsuariService;

@Controller
@SessionAttributes({ "carrito" })
public class BotigaController {

	@Autowired
	ProducteRepositori producteRepositori;

	@Autowired
	CarritoRepositori compra;

	@Autowired
	UsuariService u_service;

	@Autowired
	ProducteService p_service;

	@ModelAttribute("carrito")
	public Carrito getCarrito() {
		return new Carrito();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/dashboard")
	public String dashboard(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer page, Model model) {

		List<Producte> productos = null;
		Page<Producte> pagina = null;

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		if (page == null)
			page = 0;

		if (keyword == null) {
			pagina = producteRepositori.findAll(new PageRequest(page, 4));
		} else {
			pagina = producteRepositori.findByNom(keyword, new PageRequest(page, 4));
		}

		productos = pagina.getContent();

		model.addAttribute("pagina", page);
		model.addAttribute("juegos", productos);

		return "dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/editProd", method = RequestMethod.POST)
	public String editar(@RequestParam("_hidden_id") String id, @RequestParam("name") String nombre,
			@RequestParam("price") String price, @RequestParam("total") String quantity,
			@RequestParam("generos") String genero, @RequestParam("distribuidoras") String distribuidora,
			@RequestParam("plataformas") String plataforma, @RequestParam("edad") String edad,
			@RequestParam("activar") String activar,
			Model model/* , final RedirectAttributes redirectAttributes */) {

		Double precio = null;
		Integer cantidad = null;

		try {

			precio = Double.parseDouble(price);
			cantidad = Integer.parseInt(quantity);

		} catch (Exception e) {

			String error = "NOPE";
			model.addAttribute("error_number", error);

			return "redirect:/dashboard";
		}

		p_service.editarProd(id, nombre, precio, cantidad, genero, distribuidora, plataforma, edad, activar);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String eliminar(@PathVariable("id") String id) {

		p_service.eliminarProd(id);

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listClients")
	public String listClients(Model model) {

		List<Usuari> clientes = u_service.listUsuaris("");

		model.addAttribute("clientes", clientes);

		return "listClients";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/listClients", method = RequestMethod.POST)
	public String delClient(@RequestParam("nom_user") String n) {

		u_service.eliminarUsuari(n);

		return "redirect:/listClients";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/addproduct")
	public String newProd(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "new_product";
	}

	/*
	 * @Secured("ROLE_ADMIN")
	 * 
	 * @RequestMapping(value = "/uploadcsv", method = RequestMethod.POST) public
	 * String saveCSV(@RequestParam("file") String name, @RequestParam("file")
	 * MultipartFile file, RedirectAttributes redirectAttributes) throws
	 * IllegalStateException, IOException {
	 * 
	 * System.out.println("Entra");
	 * 
	 * 
	 * if (!file.isEmpty()) {
	 * 
	 * BufferedReader br = null;
	 * 
	 * File convFile = new File(file.getOriginalFilename());
	 * file.transferTo(convFile);
	 * 
	 * String line = "";
	 * 
	 * try {
	 * 
	 * br = new BufferedReader(new FileReader(convFile));
	 * 
	 * while ((line = br.readLine()) != null) {
	 * 
	 * System.out.println(line);
	 * 
	 * }
	 * 
	 * } catch (FileNotFoundException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } finally { if (br != null) { try
	 * { br.close(); } catch (IOException e) { e.printStackTrace(); } } } } else
	 * { redirectAttributes.addFlashAttribute("message", "You failed to upload "
	 * + name + " because the file was empty"); }
	 * 
	 * 
	 * return "redirect:/dashboard";
	 * 
	 * }
	 */

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/singleProd", method = RequestMethod.POST)
	public String saveProduct(@RequestParam("name") String nombre, @RequestParam("price") String price,
			@RequestParam("total") String quantity, @RequestParam("generos") String genero,
			@RequestParam("distribuidoras") String distribuidora, @RequestParam("plataformas") String plataforma,
			@RequestParam("edad") String edad, @RequestParam("caja") String caja, @RequestParam("juego") String juego,
			@RequestParam("escena1") String escena1, @RequestParam("escena2") String escena2,
			@RequestParam("activar") String activar, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		caja = "/images/products/" + caja;
		juego = "/images/products/" + juego;
		escena1 = "/images/products/" + escena1;
		escena2 = "/images/products/" + escena2;

		String[] imagenes = { caja, juego, escena1, escena2 };

		Double precio = null;
		Integer cantidad = null;

		try {

			precio = Double.parseDouble(price);
			cantidad = Integer.parseInt(quantity);

		} catch (Exception e) {

			String error = "NOPE";
			model.addAttribute("error_number", error);
			return "new_product";
		}

		for (String img : imagenes) {
			if (!img.contains(".")) {
				String error = "NOPE";
				model.addAttribute("error_img", error);
				return "new_product";
			}
		}

		Producte newProd = p_service.crearProducte(nombre, genero, distribuidora, plataforma, edad, cantidad, precio,
				activar, imagenes);

		if (newProd == null) {
			String error = "NOPE";
			model.addAttribute("error_product", error);
			return "new_product";
		}

		return "redirect:/dashboard";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/newAdmin")
	public String newAdmin(Model model) {

		List<Usuari> admins = u_service.listUsuaris("ROLE_ADMIN");

		model.addAttribute("admins", admins);

		return "newAdmin";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/newAdmin", method = RequestMethod.POST)
	public String saveAdmin(@RequestParam("username") String name, @RequestParam("passwd") String password,
			@RequestParam("email") String email, Model model) {

		Usuari result = u_service.crearAdmin(name, password, email);

		if (result == null) {

			String error = "NOPE";

			model.addAttribute("error", error);
			return "newAdmin";
		}

		return "redirect:/dashboard";
	}

	@RequestMapping("/account")
	public String account(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		String nom = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Usuari usuariRecuperat = u_service.buscaUsuari(nom);
		model.addAttribute("usuario", usuariRecuperat);

		return "account";
	}

	@RequestMapping(value = "/categoria/{category}/{cat_name}")
	public String categoriaProd(@PathVariable String category, @PathVariable String cat_name, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		List<Producte> juegos = p_service.buscarProductosCat(category, cat_name, "Si");

		for (Producte p : juegos) {
			if (p.getNom().length() > 20) {
				p.setNom(p.getNom().substring(0, 20) + "...");
			}
		}

		model.addAttribute("vacio", juegos.isEmpty());
		model.addAttribute("juegos", juegos);

		return "category";
	}

	// Zoom http://www.elevateweb.co.uk/image-zoom/examples

	@RequestMapping(value = "/producto/{product_id}", method = RequestMethod.GET)
	public String product(@PathVariable String product_id, Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		Producte product = p_service.buscarProdId(product_id);

		String[] values = new String[product.getCantidad()];

		for (int i = 0; i < values.length; i++) {
			values[i] = "" + (i + 1);
		}

		model.addAttribute("product", product);
		model.addAttribute("cantidad", values);

		return "product";
	}

	@RequestMapping(value = "/new_venta/{id}", method = RequestMethod.POST)
	public String addShopCart(@ModelAttribute("carrito") Carrito carrito, @PathVariable String id,
			@RequestParam("cantidad") String quantity, Model model) {

		Producte p = p_service.buscarProdId(id);
		Integer cantidad = Integer.parseInt(quantity);

		carrito.addProducto(new Sell(p, cantidad));
		model.addAttribute("carrito", carrito);

		return "redirect:/producto/" + id;
	}

	@RequestMapping("/")
	public String home(Model model) throws UnsupportedEncodingException {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "home";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String pedido(@ModelAttribute("carrito") Carrito carrito, SessionStatus status, Model model) {

		model.addAttribute("carrito",carrito);

		// compra.save(carrito);

		return "checkout";
	}
	
	@RequestMapping(value = "/delCart/{id}", method = RequestMethod.POST)
	public String delSell(@PathVariable String id, @ModelAttribute("carrito") Carrito carrito){
		
		for(int i = carrito.getSells().size() - 1; i >= 0; i--){
			if(carrito.getSells().get(i).getId().equals(id)){
				carrito.getSells().remove(i);
			}
		}
		
		if(carrito.getSells().size() == 0){
			
			carrito.setTieneCosas(false);
			
			return "redirect:/";
		}
		
		return "checkout";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(@RequestParam("nombre") String name, @RequestParam("email") String mail,
			@RequestParam("passwd") String password, @RequestParam("direccion") String address, Model model) {

		Usuari result = u_service.crearUsuari(name, password, mail, address);

		if (result == null) {

			String error = "Error: El nombre/correo ya existe!";

			model.addAttribute("error", error);
			return "register";
		}
		return "redirect:/";
	}

	@RequestMapping("/contact")
	public String contact(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "contact";
	}

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("generos", p_service.ordenarLista(p_service.listarAllProd("genero")));
		model.addAttribute("distribuidoras", p_service.ordenarLista(p_service.listarAllProd("distribuidora")));
		model.addAttribute("plataformas", p_service.ordenarLista(p_service.listarAllProd("plataforma")));

		return "about";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginRequest(@RequestParam(value = "error", required = false) String error,
			HttpServletRequest request) {

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
			return "redirect:/account";
		}

		return "login";
	}
}