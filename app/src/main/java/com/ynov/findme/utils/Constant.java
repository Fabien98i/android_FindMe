package com.ynov.findme.utils;

public class Constant {
    public static final String URL_SEARCH_TYPE =
            "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=objets-trouves-gares&q=%s&rows=100&sort=date&facet=gc_obo_gare_origine_r_name&refine.date=%s&refine.gc_obo_type_c=%s";
    public static final String URL_SEARCH_OBJECT =
            "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=objets-trouves-gares&q=%s&rows=100&sort=date&facet=gc_obo_gare_origine_r_name&refine.date=%s";
    public static final String URL_WEBSITE_SNCF =
            "https://www.garesetconnexions.sncf/fr/service-client/a-vos-cotes/objet-perdu-trouve";

    public static final String URL2 = "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=objets-trouves-gares&q&rows=40&facet=gc_obo_gare_origine_r_name&refine.date=%s";
    // public static final String URL = "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=objets-trouves-gares&q=%s&rows=25&sort=date";
    // public static final String URL = "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=objets-trouves-gares&q=&rows=25&facet=date&facet=gc_obo_gare_origine_r_name&facet=gc_obo_type_c&refine.gc_obo_gare_origine_r_name=%s&refine.date=2020-12-28";
}
