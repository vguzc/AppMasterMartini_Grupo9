package com.example.appmastermartini_grupo9.repository

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
                description = "Chocolates puros de origen (Costa de Marfil) en discos y gotas. Formatos 72% amargo, leche y blanco.",
                tag = "Línea Ariba"
            ),
            ProductItem(
                id = "caravella-cream",
                name = "Caravella Avellana y Cacao",
                category = "Coberturas y Cremas",
                description = "Cremas para relleno y horneo, untables y coberturas de alto rendimiento para pastelería fina.",
                tag = "Línea Caravella"
            ),
            ProductItem(
                id = "decor-up",
                name = "Decor Up Crema UHT",
                category = "Cremas Vegetales",
                description = "Crema vegetal para batir de altísima estabilidad y rendimiento, resistente a la congelación.",
                tag = "Línea Decor Up"
            ),
            ProductItem(
                id = "orasi-barista",
                name = "OraSì Bebida Vegetal Barista",
                category = "Plant Based / Cafetería",
                description = "Bebidas 100% vegetales (almendra, soya, avena y coco), formuladas especialmente para Latte Art.",
                tag = "Línea OraSì"
            )
        )
    }
}
