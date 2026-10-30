package com.example.appmastermartini_grupo9.repository

import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.ProductItem

/**
 * Repositorio que suministra los datos del catálogo profesional de Master Martini Chile.
 */
class ProductRepository {

    fun getFeaturedProducts(): List<ProductItem> {
        return listOf(
            ProductItem(
                id = "ariba-choc",
                name = "Ariba Chocolate Puro",
                category = "Chocolatería",
                description = "Chocolates puros de manteca de cacao seleccionada (origen Costa de Marfil) en discos Diamante y gotas. Fluidez precisa, contracción limpia y brillo espejo.",
                tag = "Línea Ariba",
                formato = "Bolsas de 1 kg, 2.5 kg y cajas de 10 kg (72%, Leche 36% y Blanco)",
                aplicacion = "Bombonería fina, tabletas, mousses y baños de alta pastelería",
                imagenRes = R.drawable.receta_cioccolato
            ),
            ProductItem(
                id = "caravella-cream",
                name = "Caravella Avellana y Cacao",
                category = "Cremas y Coberturas",
                description = "Cremas anhidras untables, rellenos pre y post-horneo y coberturas sucedáneas de altísima trabajabilidad sin necesidad de templado.",
                tag = "Línea Caravella",
                formato = "Baldes de 5 kg y 13 kg • Discos de 2.5 kg",
                aplicacion = "Rellenos de bollería, copas gianduja, glaseados y bañados rápidos",
                imagenRes = R.drawable.receta_pasteleria
            ),
            ProductItem(
                id = "decor-up",
                name = "Decor Up Crema UHT",
                category = "Cremas Vegetales",
                description = "Crema vegetal UHT ligeramente azucarada de altísima estabilidad. Cuadruplica su volumen al batir, no presenta sinéresis y resiste congelación.",
                tag = "Línea Decor Up",
                formato = "Envase Tetra Brik 1 Litro (cajas de 12 unidades)",
                aplicacion = "Decoración con manga, entremets, semifríos y rellenos estables",
                imagenRes = R.drawable.receta_ricette_hero
            ),
            ProductItem(
                id = "orasi-barista",
                name = "OraSì Bebida Vegetal Barista",
                category = "Plant Based",
                description = "Bebidas 100% vegetales (Avena, Almendra, Soya y Coco) desarrolladas junto a baristas y heladeros para lograr microespuma elástica y sorbetes cremosos.",
                tag = "Línea OraSì",
                formato = "Tetra Brik 1 Litro • Sin lactosa y 100% vegetal",
                aplicacion = "Latte Art profesional, pastelería vegana y sorbetes italianos",
                imagenRes = R.drawable.receta_gelato
            ),
            ProductItem(
                id = "maxime-butter",
                name = "Margarina Maxime Hojaldre",
                category = "Margarinas",
                description = "Margarina en placas de plasticidad controlada y delicado bouquet lácteo, formulada para laminados parejos y alveolado crujiente.",
                tag = "Línea Maxime",
                formato = "Placas de 2 kg (cajas de 10 kg)",
                aplicacion = "Croissants, danish rolls, milhojas y masas danesas",
                imagenRes = R.drawable.receta_frozen
            )
        )
    }
}
