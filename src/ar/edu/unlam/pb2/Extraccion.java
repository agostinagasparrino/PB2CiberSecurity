package ar.edu.unlam.pb2;

import java.util.*;

public class Extraccion extends Monetaria implements Rechazable, Alertable {

	private Integer score;
	private Boolean casoSospechoso;
	private Boolean fueFraude;
	
	public Extraccion(Cliente cliente, Dispositivo dispositivo, Double monto) {
		super(cliente, dispositivo, monto);
		this.score = 0;
	}

	@Override
	public Integer calcularScore(Set<Denunciable> listaNegra) {
		
		
		if(!super.getQuienLaRealiza().existeDispositivo(super.getDesdeDondeSerealiza())) {
			score+=Rechazable.SCORE_DISPOSITIVO_NUEVO;
		}
		if(super.getQuienLaRealiza().getSaldo().equals(super.getMonto())) {
			score+=Rechazable.SCORE_IMPORTE_IGUAL_AL_SALDO;
		}
		if(listaNegra.contains(super.getQuienLaRealiza())) {
			score+=Rechazable.SCORE_CUIT_INVOLUCRADO_EN_FRAUDE_PREVIO;
		}
		if(listaNegra.contains(super.getDesdeDondeSerealiza())) {
			score+=Rechazable.SCORE_CUIT_INVOLUCRADO_EN_FRAUDE_PREVIO;
		}
		
		return this.score;
	}

	@Override
	public void monitorear(Set<Denunciable> listaNegra) throws FraudeException{
		this.score = this.calcularScore(listaNegra);
		super.getQuienLaRealiza().agregarDispositivo(super.getDesdeDondeSerealiza());
		
		if(this.score>=Rechazable.UMBRAL_RECHAZO) {
			throw new FraudeException();
		}
		else if(this.score>=Rechazable.UMBRAL_ALERTA) {
			this.setCasoSospechoso(true);
		}
	}

	@Override
	public Integer getScore() {
		// TODO Auto-generated method stub
		return score;
	}

	@Override
	public void marcarComoCasoSospechoso() {
		this.setCasoSospechoso(true);		
	}

	@Override
	public void confirmarSiFueFraude(Boolean fueFraude) {
		this.setFueFraude(true);
	}

	public void setCasoSospechoso(Boolean casoSospechoso) {
		this.casoSospechoso = casoSospechoso;
	}

	public void setFueFraude(Boolean fueFraude) {
		this.fueFraude = fueFraude;
	}

	@Override
	public Boolean getCasoSospechoso() {
		return casoSospechoso;
	}

	@Override
	public Boolean getFueFraude() {
		return fueFraude;
	}

}
