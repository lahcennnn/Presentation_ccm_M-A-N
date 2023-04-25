package com.cedicam.gm.re.dto;

import com.cedicam.gm.re.data.dto.EntiteDto;

public class ExtendedEntiteDto extends EntiteDto {
	
	/** Serial version UID. */
	private static final long serialVersionUID = -1653448616237686729L;

	public ExtendedEntiteDto(EntiteDto entiteDto) {
		super();
		
		this.setCdBanqueEntACompt(entiteDto.getCdBanqueEntACompt());
		this.setChefDeFile(entiteDto.getChefDeFile());
		this.setCirc1CdBqReglCore(entiteDto.getCirc1CdBqReglCore());
		this.setCirc1CdBqReglCup(entiteDto.getCirc1CdBqReglCup());
		this.setCirc1CdBqReglInter(entiteDto.getCirc1CdBqReglInter());
		this.setCirc1CdBqReglIntra(entiteDto.getCirc1CdBqReglIntra());
		this.setCirc1CdBqReglMci(entiteDto.getCirc1CdBqReglMci());
		this.setCirc1CdBqReglVisa(entiteDto.getCirc1CdBqReglVisa());
		this.setClientExterneGroupeCa(entiteDto.getClientExterneGroupeCa());
		this.setCodeBanque(entiteDto.getCodeBanque());
		this.setCodeIcaMci(entiteDto.getCodeIcaMci());
		this.setDateCreation(entiteDto.getDateCreation());
		this.setDateMaj(entiteDto.getDateMaj());
		this.setDtDbEffet(entiteDto.getDtDbEffet());
		this.setDtDeMigrEntSurBoa(entiteDto.getDtDeMigrEntSurBoa());
		this.setDtDeMigrEntSurBoe(entiteDto.getDtDeMigrEntSurBoe());
		this.setDtFinEffet(entiteDto.getDtFinEffet());
		this.setEnvImCrrEntComp(entiteDto.getEnvImCrrEntComp());
		this.setEnvImCrrEntEch(entiteDto.getEnvImCrrEntEch());
		this.setLibelle(entiteDto.getLibelle());
		this.setUserCreation(entiteDto.getUserCreation());
		this.setUserMaj(entiteDto.getUserMaj());
		this.setVersion(entiteDto.getVersion());
	}

    public boolean allPropertiesEqual(EntiteDto other) {
    	boolean equal = true;
    	
        if (!propertiesEqual(getCodeBanque(), other.getCodeBanque()) || !propertiesEqual(getDtDbEffet(), other.getDtDbEffet())
        		|| !propertiesEqual(getVersion(), other.getVersion()) || !propertiesEqual(getLibelle(), other.getLibelle())
        		|| !propertiesEqual(getClientExterneGroupeCa(), other.getClientExterneGroupeCa()) 
        		|| !propertiesEqual(getDtDeMigrEntSurBoe(), other.getDtDeMigrEntSurBoe())
        		|| !propertiesEqual(getDtDeMigrEntSurBoa(), other.getDtDeMigrEntSurBoa()) 
        		|| !propertiesEqual(getCdBanqueEntACompt(), other.getCdBanqueEntACompt())
        		|| !propertiesEqual(getEnvImCrrEntEch(), other.getEnvImCrrEntEch()) || !propertiesEqual(getEnvImCrrEntComp(), other.getEnvImCrrEntComp())
        		|| !propertiesEqual(getCodeIcaMci(), other.getCodeIcaMci()) || !propertiesEqual(getCirc1CdBqReglIntra(), other.getCirc1CdBqReglIntra())
        		|| !propertiesEqual(getCirc1CdBqReglInter(), other.getCirc1CdBqReglInter()) || !propertiesEqual(getCirc1CdBqReglCore(), other.getCirc1CdBqReglCore())
        		|| !propertiesEqual(getCirc1CdBqReglCup(), other.getCirc1CdBqReglCup()) || !propertiesEqual(getCirc1CdBqReglMci(), other.getCirc1CdBqReglMci())
        		|| !propertiesEqual(getCirc1CdBqReglVisa(), other.getCirc1CdBqReglVisa()) || !propertiesEqual(getDtFinEffet(), other.getDtFinEffet())
        		|| !propertiesEqual(getChefDeFile(), other.getChefDeFile()) || !propertiesEqual(getDateCreation(), other.getDateCreation())
        		|| !propertiesEqual(getUserCreation(), other.getUserCreation()) || !propertiesEqual(getDateMaj(), other.getDateMaj())
        		|| !propertiesEqual(getUserMaj(), other.getUserMaj())) {
        	equal = false;
        }
        
    	return equal;
    }
    
    private boolean propertiesEqual(Object property, Object toCompare) {
    	return property == null && toCompare == null || property != null && property.equals(toCompare);
    }
}
