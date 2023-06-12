package com.clinica.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinica.entity.Boleta;
import com.clinica.entity.Cliente;
import com.clinica.entity.Detalle;
import com.clinica.entity.Medicamento;
import com.clinica.entity.MedicamentoHasBoleta;
import com.clinica.entity.Usuario;
import com.clinica.service.BoletaService;
import com.clinica.service.ClienteService;
import com.clinica.service.MedicamentoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/boleta")
public class BoletaController {
	@Autowired
	private ClienteService serCliente;
	@Autowired
	private MedicamentoService serMed;
	@Autowired
	private BoletaService servBoleta;
	@RequestMapping("/lista")
	public String lista(){
		
		return "boleta";
	}
	
	@RequestMapping("/listaJSON")
	@ResponseBody
	public List<Cliente> listaJSON(){
		return serCliente.listaClientes();
	}
	
	@RequestMapping("/listaPaternoJSON")
	@ResponseBody
	public List<Cliente> listaPaternoJSON(@RequestParam("paterno") String paterno){
		return serCliente.listarClienteXPaterno(paterno);
	}
	
	@RequestMapping("/listaTodosMEdJSON")
	@ResponseBody
	public List<Medicamento> listaEdicamentoJSON(){
		return serMed.listarTodos();
	}
	
	@RequestMapping("/listaMedicamentoJSON")
	@ResponseBody
	public List<Medicamento> listaMedxNombreJSON(@RequestParam("nombre") String nombre){
		return serMed.listarPorNombre(nombre);
	}
	
	
	@RequestMapping("/adicionar")
	@ResponseBody
	public List<Detalle> adicionar(@RequestParam("codigo") int cod,
							@RequestParam("descripcion") String des,
							@RequestParam("precio") double pre,
							@RequestParam("cantidad") int can,
							HttpSession session){
		//Declarar un arreglo de objeto de la clase detalle
		List<Detalle> lista=null;
		try {
			//validad si existe el atributo de tipo session "data"
			if(session.getAttribute("data")==null) 
				//crear arreglo lista
				lista =new ArrayList<Detalle>();
			else //existe atributo data
				//recuperar el atributo "data" u guardar su contenido en lista
				lista = (List<Detalle>) session.getAttribute("data");
			
				//crear objeto de la clase detalle
				Detalle det = new Detalle();
				//setear atributos del objeto "det" con los valores de los parametros
				det.setCodigo(cod);
				det.setDescripcion(des);
				det.setCantidad(can);
				det.setPrecio(pre);
				//adicionar objeto "det" dentro del arreglo "lista"
				lista.add(det);
			

			//crear atributo de tipo session "data"
			session.setAttribute("data",lista);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	
	@RequestMapping("/grabar")
	public String grabar(@RequestParam("cliente") String cliente,
						@RequestParam("fecha")String fecha,
						@SessionAttribute("CODIGO_USUARIO") int codUsu,
						HttpSession session,
						RedirectAttributes redirect){
		try {
			//crear objeto de la entidad Boleta
			Boleta bol= new Boleta();
			//setear bol
			bol.setMonto(200.50);
			bol.setFechaEmision(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			
			Usuario u = new Usuario();
			u.setCodigo(codUsu);
			//adionar objeto "u" dentro de bol
			bol.setUsuario(u);
			//separar el valor de cliente
			//parametro clie => 2-nombre
			String sep[]=cliente.split("-");
			//sep[0] = "2"         sep[1]=nombre
			//adicionar objeto de la entidad Cliente
			Cliente c = new Cliente();
			c.setCodigo(Integer.parseInt(sep[0]));
			bol.setCliente(c);
			//detalle
			//crear un arreglo de objetos de la Entidad MedicamentoHasBoleta
			List<MedicamentoHasBoleta> lista =new ArrayList<MedicamentoHasBoleta>();
			//recuperar el atributo de tipo sesion "data"
			List<Detalle> datos= (List<Detalle>) session.getAttribute("data");
			//bucle para realizar 
			for(Detalle det:datos) {
				//crear objetode la entidad medicamentoHasBoleta
				MedicamentoHasBoleta mhb= new MedicamentoHasBoleta();
				//Crear objeto de la entidad Medicamento
				Medicamento med = new Medicamento();
				//setear med
				med.setCodigo(det.getCodigo());
				//enviar objeto "med" al objeto "mhb"
				mhb.setMedicamento(med);
				mhb.setPrecio(det.getPrecio());
				
				//enviar el objeto "mhb" al arreglo "lista"
				lista.add(mhb);
			}
			//enviar el arreglo "lista" al atributo listaMedicamentoHasBoleta
			bol.setListaMedicamentoHasBoleta(lista);
			//
			servBoleta.registrar(bol);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/boleta/lista";
	}
}
