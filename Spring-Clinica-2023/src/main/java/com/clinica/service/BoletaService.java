package com.clinica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinica.dao.BoletaRepository;
import com.clinica.dao.MedicamentoHasBoletaRepository;
import com.clinica.entity.Boleta;
import com.clinica.entity.MedicamentoHasBoleta;
import com.clinica.entity.MedicamentoHasBoletaPK;

import jakarta.transaction.Transactional;

@Service
public class BoletaService {

	@Autowired
	private BoletaRepository repoBoleta;
	
	@Autowired
	private MedicamentoHasBoletaRepository repoMedBol;
	
	@Transactional
	public void registrar(Boleta bean) {
		try {
			//Grabar Boleta "Cabezera -->tb_boleta" 
			repoBoleta.save(bean);
			
			//Grabar MedicamenHasBotela "Detalle --> tb_medicamento_has_boleta"
			//bucle para realizar recorrido sobre el atributo "listaMedicamentoHasBoleta" del objeto "bean"
			for(MedicamentoHasBoleta mhb:bean.getListaMedicamentoHasBoleta()) {
				//llave
				MedicamentoHasBoletaPK pk= new MedicamentoHasBoletaPK();
				//setear
				pk.setCodigoMedicamento(mhb.getMedicamento().getCodigo());
				pk.setNumeroBoleta(bean.getNumeroBoleta());
				//enviar "pk" a mhb
				mhb.setPk(pk);
				//grabar mhb
				repoMedBol.save(mhb);				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
