package com.lightningkite.mppexampleapp

val products = listOf(
    Product(
        name = "IC Surge Device",
        key = "ic-surge-device",
        image = "https://picsum.photos/seed/a/200",
        description = "The IC Surge Device is designed to protect the IC System from lightning strikes and power surges. The device is installed between the IC System and the decoder cable. The device is designed to protect the IC System from lightning strikes and power surges. The device is installed between the IC System and the decoder cable.",
        price = 100.0
    ),
    Product(
        name = "IC Module",
        key = "ic-module",
        image = "https://picsum.photos/seed/b/200",
        description = "The IC Module is the heart of the IC System. It is the interface between the IC Central Control and the field decoders. The IC Module is the heart of the IC System. It is the interface between the IC Central Control and the field decoders.",
        price = 200.0
    )
)

val rootCategory = Category(
    name = "Dashboard",
    image = "https://picsum.photos/seed/a/200",
    key = "dashboard",
    subcategories = listOf(
        Category(
            name = "Golf Irrigation",
            key = "golf-irrigation",
            image = "https://picsum.photos/seed/b/200",
            subcategories = listOf(
                Category(
                    name = "Control Systems",
                    key = "control-systems",
                    image = "https://picsum.photos/seed/a/200",
                    subcategories = listOf(
                        Category(
                            name = "IC - Integrated Control",
                            key = "ic-integrated-control",
                            image = "https://picsum.photos/seed/a/200",
                            products = products
                        ),
                        Category(
                            name = "Satellite Controllers",
                            key = "satellite-controllers",
                            image = "https://picsum.photos/seed/b/200",
                            products = products
                        ),
                        Category(
                            name = "Decoders",
                            key = "decoders",
                            image = "https://picsum.photos/seed/c/200",
                            products = products
                        ),
                        Category(
                            name = "Accessories",
                            key = "accessories",
                            image = "https://picsum.photos/seed/d/200",
                            products = products
                        ),
                    )
                ),
                Category(
                    name = "Gear Rotors",
                    key = "gear-rotors",
                    image = "https://picsum.photos/seed/b/200",
                    products = products
                ),
                Category(
                    name = "Impact Sprinklers",
                    key = "impact-sprinklers",
                    image = "https://picsum.photos/seed/c/200",
                    products = products
                ),
                Category(
                    name = "Valves",
                    key = "valves",
                    image = "https://picsum.photos/seed/d/200",
                    products = products
                ),
                Category(
                    name = "Valve Boxes",
                    key = "valve-boxes",
                    image = "https://picsum.photos/seed/e/200",
                    products = products
                ),
                Category(
                    name = "Swing Joints",
                    key = "swing-joints",
                    image = "https://picsum.photos/seed/f/200",
                    products = products
                ),
                Category(
                    name = "Pipe",
                    key = "pipe",
                    image = "https://picsum.photos/seed/a/200",
                    products = products
                ),
                Category(
                    name = "Fittings",
                    key = "fittings",
                    image = "https://picsum.photos/seed/b/200",
                    products = products
                ),
                Category(
                    name = "Wire",
                    key = "wire",
                    image = "https://picsum.photos/seed/c/200",
                    products = products
                ),
                Category(
                    name = "Audit Kits",
                    key = "audit-kits",
                    image = "https://picsum.photos/seed/d/200",
                    products = products
                ),
                Category(
                    name = "Irrigation Accessories",
                    key = "irrigation-accessories",
                    image = "https://picsum.photos/seed/e/200",
                    products = products
                ),
                Category(
                    name = "Tools",
                    key = "tools",
                    image = "https://picsum.photos/seed/f/200",
                    products = products
                ),
            )
        ),
        Category(
            name = "Res/Com Irrigation",
            key = "res-com-irrigation",
            image = "https://picsum.photos/seed/c/200",
            products = products
        ),
        Category(
            name = "Agriculture Irrigation",
            key = "agriculture-irrigation",
            image = "https://picsum.photos/seed/d/200",
            products = products
        ),
        Category(
            name = "Drainage",
            key = "drainage",
            image = "https://picsum.photos/seed/e/200",
            products = products
        ),
        Category(
            name = "Landscape Lighting",
            key = "landscape-lighting",
            image = "https://picsum.photos/seed/f/200",
            products = products
        ),
        Category(
            name = "Ponds",
            key = "ponds",
            image = "https://picsum.photos/seed/g/200",
            products = products
        ),
        Category(
            name = "Landscape",
            key = "landscape",
            image = "https://picsum.photos/seed/h/200",
            products = products
        ),
        Category(
            name = "Clearance",
            key = "clearance",
            image = "https://picsum.photos/seed/i/200",
            products = products
        ),
    )
)

class Dashboard : CategoryScreen(
    category = rootCategory
) {

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/dashboard"

        fun create(): Dashboard = Dashboard()
    }
}
