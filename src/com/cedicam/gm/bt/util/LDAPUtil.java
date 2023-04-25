package com.cedicam.gm.bt.util;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * @author Administrateur
 *
 */
public class LDAPUtil {
	
	/** prefix pour dn. */
	private static String PREFIX_DN = "/";
	/** prefix pour le lien vers serveur. */
	private static String PREFIX_HOST = "ldap://";

	/** dn. */
    private String dn; 
    /** host. */
    private String host;
    /** ctx. */
    private DirContext ctx;

    /** Constructeur */
	public LDAPUtil() {

	}

	/**
	 * @param dn the dn to set
	 */
	public void setDn(String dn) {
		this.dn = dn;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param ctx the ctx to set
	 */
	public void setCtx(DirContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * <I>Procedure de test de connexion.</I><br>
	 * <p><b>@return boolean</b></p>
	 * <p><b>@param loginUrl String le login</b></p>
	 * <p><b>@param password Le mot de passe</b></p>
	 */
	public boolean testeConnexionLDAP(final String loginUrl, 
			final String password) {
		boolean isConnexionEtablie = false;
		// TODO IP et Url temporaires
		String serverIP = "swincacpadlds01";
		String serverPort = ":1389";
		this.host = PREFIX_HOST + serverIP + serverPort;
		this.dn = PREFIX_DN + "CN=capsmxa,OU=appusers,OU=qualification,O=cardspayments,O=creditagricole,O=fr";
		this.ctx = this.getContext(loginUrl, password);
		if (this.ctx != null) {
			try {
				this.ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			isConnexionEtablie = true;
		}
		return isConnexionEtablie;
	}

	/**
	 * <I>Procedure de test de connexion.</I><br>
	 * <p><b>@return boolean</b></p>
	 * <p><b>@param loginUrl String le login</b></p>
	 * <p><b>@param password Le mot de passe</b></p>
	 */
	public ArrayList<String> getRoles(final String loginUrl, 
			final String password) {
		ArrayList<String> roles = new ArrayList<String>();
		// TODO IP et Url temporaires
		String serverIP = "swincacpadlds01";
		String serverPort = ":1389";
		this.host = PREFIX_HOST + serverIP + serverPort;
		this.dn = PREFIX_DN + "CN=capsmxa,OU=appusers,OU=qualification,O=cardspayments,O=creditagricole,O=fr";
		this.ctx = this.getContext(loginUrl, password);

		//Create the search controls 		
		SearchControls searchCtls = new SearchControls();
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//specify the LDAP search filter
		String searchFilter = "CN=Roles"; // "(&(objectClass=user)(cn="+pUsername+"))";
		//Specify the Base for the search
		String searchBase = "";
		if (this.ctx != null) {
			try {
				NamingEnumeration<SearchResult> searchResult = this.ctx.search(
						searchBase, searchFilter, searchCtls);
				if (searchResult != null && searchResult.hasMoreElements()){
					SearchResult result = (SearchResult) searchResult.next();
					if (result != null){
						roles.add(result.toString());
					}
				}
				this.ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return roles;
	}

	/**
	 * <I>getContext, retourne le contexte LDAP.</I><br>
	 * <p><b>@return InitialLdapContext</b></p>
	 * <p><b>@param loginUrl String le login</b></p>
	 * <p><b>@param password Le mot de passe</b></p>
	 */
	private DirContext getContext(final String loginUrl, 
			final String password) {
		DirContext context = null;
		Hashtable<String, String> env = this.loadEnv(loginUrl, password);
        // Create the initial context
        try {
        	context = new InitialDirContext(env);
        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return context;
	}

    /*
     * Préparation du tableau de paramètres 
     * envoyés au contexte Directory. 
     */
    private Hashtable<String, String> loadEnv() {
    	Hashtable<String, String> env = new Hashtable<String, String>();
    	env.put(Context.INITIAL_CONTEXT_FACTORY, 
    			"com.sun.jndi.ldap.LdapCtxFactory");
    	env.put(Context.PROVIDER_URL, this.host + this.dn);
    	env.put(Context.SECURITY_AUTHENTICATION, "simple");
    	env.put(Context.SECURITY_PROTOCOL, "none");
        return env;
    }

    /*
     * Préparation du tableau de paramètres 
     * envoyés au contexte Directory. 
     */
    private Hashtable<String, String> loadEnv(final String loginUrl, 
    		final String password) {
    	Hashtable<String, String> env = this.loadEnv();
    	if (loginUrl != null && password != null) {
    		env.put(Context.SECURITY_PRINCIPAL, loginUrl);
    		env.put(Context.SECURITY_CREDENTIALS, password);
		}
    	return env;
    }

}
