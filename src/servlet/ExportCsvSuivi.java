package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cedicam.gm.cm.bean.SuiviBean;

/** * Servlet implementation class TestService */
public class ExportCsvSuivi extends GenericServlet {

	private static final long serialVersionUID = -7665472027630822612L;
	com.cedicam.gm.cm.bean.SuiviBean sb;

	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sb = (SuiviBean)getBean("suivibean",request, response);
		StringBuffer resultat = new StringBuffer();
		printSearch(resultat);

//		if(sb.getHasDetail()){
//			printDetail(resultat);
//		}else if (sb.getHasRegroupements()){
//			printRegroupements(resultat);
//		}else if (sb.getHasRapprochements()){
//			printRapprochements(resultat);
//		}else if (sb.getHasSyntheseEcart()){
//			printSyntheseEcart(resultat);
//		}

		ServletOutputStream op = response.getOutputStream();

		response.setContentType("application/octet-stream" );
		response.setContentLength(resultat.length());
		response.setHeader( "Content-Disposition", "attachment; filename=\"export_ccm.csv\"" );
		op.write(resultat.toString().getBytes());
		op.flush();
		op.close();


		//response.setHeader("Content-Type", "text/csv");
		//response.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
		//PrintWriter writer = response.getWriter();
		//writer.print(resultat);
		//writer.flush();
		//writer.close();
	}

	private void printSyntheseEcart(StringBuffer resultat){
		resultat.append("Entité;Boa;Boa\n");
//		Map<String,ValeurEcartDto> map = sb.getEcart().getEcarts();
//		for(String key: map.keySet()){
//			ValeurEcartDto valeur = map.get(key);
//			addField(key, resultat);
//			addField(valeur.isEcartBoa()?"Ok":"KO", resultat);
//			addField(valeur.isEcartBoe()?"Ok":"KO", resultat);
//			resultat.append("\n");
//		}
	}
	private void addField(int s, StringBuffer buf){
		buf.append(s);
		buf.append(";");
	}

	private void addField(double s, StringBuffer buf){
		buf.append(s);
		buf.append(";");
	}

	private void addField(String s, StringBuffer buf){
		if(s!=null)buf.append(s);
		buf.append(";");
	}

	private void printRapprochements(StringBuffer resultat){
		resultat.append("Rapprochements\n");
		resultat.append("Code opération;Entite émetteur;Regroupement émission;;;Entité destinataire;Regroupement réception;;;Ecart;;Règlement;;;Statut;\n");
		resultat.append(";;Nombre;Signe;Montant;;Nombre;Signe;Montant;Nombre;Signe;Montant;Nombre;Signe;Montant;\n");

//		for(RapprochementDto r:sb.getRapprochements()){
//			addField(r.getCodeOperation(),resultat);
//
//			ValeurDto val = r.getEmission();
//			if(val==null)resultat.append(";;;;");
//			else{
//				addField(val.getEntite(),resultat);
//				addField(val.getNbOperations(),resultat);
//				addField(val.getSigne(),resultat);
//				addField(val.getMontant(),resultat);
//			}
//
//			val = r.getReception();
//			if(val==null)resultat.append(";;;;");
//			else{
//				addField(val.getEntite(),resultat);
//				addField(val.getNbOperations(),resultat);
//				addField(val.getSigne(),resultat);
//				addField(val.getMontant(),resultat);
//			}
//
//			val = r.getEcart();
//			if(val==null)resultat.append(";;;;");
//			else{
//				addField(val.getNbOperations(),resultat);
//				addField(val.getSigne(),resultat);
//				addField(val.getMontant(),resultat);
//			}
//
//			val = r.getReglement();
//			if(val==null)resultat.append(";;;;");
//			else{
//				addField(val.getNbOperations(),resultat);
//				addField(val.getSigne(),resultat);
//				addField(val.getMontant(),resultat);
//			}
//
//			addField(r.getStatut(),resultat);
//			resultat.append("\n");
//		}

	}

	private void printRegroupements(StringBuffer resultat){
		resultat.append("Regroupements émetteurs;Référence comptable;Zone rapprochement;IdFichier échange;\n");
//		printRegroupements(resultat, sb.getRegroupementsEmetteur());
//		resultat.append("\n\n");
//		resultat.append("Regroupements destinataires;Référence comptable;Zone rapprochement;IdFichier échange;\n");
//		printRegroupements(resultat, sb.getRegroupementsDestinataire());
	}

//	private void printRegroupements(StringBuffer resultat, List<RegroupementDto> list){
//		for(RegroupementDto r: list){
//			addField(r.getLibelle(),resultat);
//			addField(r.getReferenceComptable(),resultat);
//			addField(r.getZoneRapprochement(),resultat);
//			addField(r.getIdFicherEchange(),resultat);
//			resultat.append("\n");
//		}
//	}


	private void printDetail(StringBuffer resultat){
		resultat.append("Détails;\n");
//		for(DetailDto d: sb.getDetails()){
//			addField(d.getLigne(), resultat);
//			resultat.append("\n");
//		}
	}

	private void printSearch(StringBuffer resultat){
		// la recherche n'est pas exportée
	}

}
