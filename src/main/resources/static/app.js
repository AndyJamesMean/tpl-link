const state = {
    products: [],
    cart: { items: [], totalQuantity: 0, totalAmount: 0 },
    orders: [],
    keyword: "",
    category: "all"
};

const categoryMap = {
    phone: "digital",
    keyboard: "digital",
    kettle: "life",
    pillow: "life",
    bag: "style",
    hoodie: "style"
};

const productGrid = document.querySelector("#productGrid");
const cartItems = document.querySelector("#cartItems");
const cartCount = document.querySelector("#cartCount");
const cartTotal = document.querySelector("#cartTotal");
const orderList = document.querySelector("#orderList");
const checkoutForm = document.querySelector("#checkoutForm");
const checkoutBtn = document.querySelector("#checkoutBtn");
const searchInput = document.querySelector("#searchInput");
const clearCartBtn = document.querySelector("#clearCartBtn");
const refreshOrdersBtn = document.querySelector("#refreshOrdersBtn");
const toast = document.querySelector("#toast");

async function api(path, options = {}) {
    const config = {
        method: options.method || "GET",
        headers: options.body ? { "Content-Type": "application/json" } : {},
        body: options.body
    };
    const response = await fetch(path, config);
    const data = await response.json().catch(() => null);
    if (!response.ok) {
        throw new Error(data && data.message ? data.message : "请求失败");
    }
    return data;
}

function money(value) {
    return `¥${Number(value || 0).toLocaleString("zh-CN", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })}`;
}

function escapeHtml(value) {
    return String(value == null ? "" : value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function mockClass(theme) {
    const allowed = ["phone", "bag", "kettle", "pillow", "keyboard", "hoodie"];
    return allowed.includes(theme) ? theme : "phone";
}

function showToast(message) {
    toast.textContent = message;
    toast.classList.add("show");
    window.clearTimeout(showToast.timer);
    showToast.timer = window.setTimeout(() => toast.classList.remove("show"), 2200);
}

function filteredProducts() {
    const keyword = state.keyword.trim().toLowerCase();
    return state.products.filter((product) => {
        const inCategory = state.category === "all" || categoryMap[product.imageTheme] === state.category;
        const inKeyword = !keyword
            || product.title.toLowerCase().includes(keyword)
            || product.shopName.toLowerCase().includes(keyword);
        return inCategory && inKeyword;
    });
}

function renderProducts() {
    const products = filteredProducts();
    if (products.length === 0) {
        productGrid.innerHTML = `<div class="empty-state">没有匹配的商品</div>`;
        return;
    }

    productGrid.innerHTML = products.map((product) => {
        const theme = mockClass(product.imageTheme);
        const disabled = product.stock <= 0 ? "disabled" : "";
        const buttonText = product.stock <= 0 ? "已售罄" : "加入购物车";
        return `
            <article class="product-card">
                <div class="product-visual theme-${theme}">
                    <span class="mock ${theme}" aria-hidden="true"></span>
                </div>
                <div class="product-body">
                    <div class="price-line">
                        <strong class="price">${money(product.price)}</strong>
                        <span class="tag" title="${escapeHtml(product.tag)}">${escapeHtml(product.tag)}</span>
                    </div>
                    <div>
                        <h3 class="product-title">${escapeHtml(product.title)}</h3>
                        <p class="product-meta">${escapeHtml(product.shopName)} · 已售 ${product.soldCount}</p>
                    </div>
                    <p class="product-desc">${escapeHtml(product.description)}</p>
                    <button class="add-cart" type="button" data-product-id="${product.id}" ${disabled}>
                        ${buttonText}
                    </button>
                </div>
            </article>
        `;
    }).join("");
}

function renderCart() {
    cartCount.textContent = state.cart.totalQuantity || 0;
    cartTotal.textContent = money(state.cart.totalAmount);
    checkoutBtn.disabled = !state.cart.items.length;
    clearCartBtn.disabled = !state.cart.items.length;

    if (!state.cart.items.length) {
        cartItems.innerHTML = `<div class="empty-state">购物车为空</div>`;
        return;
    }

    cartItems.innerHTML = state.cart.items.map((item) => `
        <div class="cart-row">
            <div>
                <p class="cart-row-title">${escapeHtml(item.title)}</p>
                <div class="cart-row-meta">${money(item.unitPrice)} · 库存 ${item.stock}</div>
            </div>
            <div class="quantity-control" aria-label="${escapeHtml(item.title)}数量">
                <button class="icon-button" type="button" title="减少"
                        data-cart-action="decrease" data-product-id="${item.productId}"
                        data-quantity="${item.quantity}">−</button>
                <span class="quantity-value">${item.quantity}</span>
                <button class="icon-button" type="button" title="增加"
                        data-cart-action="increase" data-product-id="${item.productId}"
                        data-quantity="${item.quantity}">+</button>
                <button class="icon-button" type="button" title="移除"
                        data-cart-action="remove" data-product-id="${item.productId}">×</button>
            </div>
        </div>
    `).join("");
}

function renderOrders() {
    if (!state.orders.length) {
        orderList.innerHTML = `<div class="empty-state">暂无订单</div>`;
        return;
    }

    orderList.innerHTML = state.orders.map((order) => {
        const createdAt = order.createdAt ? order.createdAt.replace("T", " ").slice(0, 19) : "";
        const items = order.items.map((item) => `
            <li>${escapeHtml(item.title)} × ${item.quantity}</li>
        `).join("");
        return `
            <article class="order-card">
                <div>
                    <h3>${escapeHtml(order.orderNo)} · ${escapeHtml(order.status)}</h3>
                    <div class="order-meta">${escapeHtml(order.buyerName)} · ${escapeHtml(order.phone)} · ${createdAt}</div>
                    <div class="order-meta">${escapeHtml(order.address)}</div>
                    <ul class="order-items">${items}</ul>
                </div>
                <div class="order-amount">${money(order.totalAmount)}</div>
            </article>
        `;
    }).join("");
}

async function loadAll() {
    const [products, cart, orders] = await Promise.all([
        api("/api/products"),
        api("/api/cart"),
        api("/api/orders")
    ]);
    state.products = products;
    state.cart = cart;
    state.orders = orders;
    renderProducts();
    renderCart();
    renderOrders();
}

async function addToCart(productId) {
    state.cart = await api("/api/cart/items", {
        method: "POST",
        body: JSON.stringify({ productId: Number(productId), quantity: 1 })
    });
    renderCart();
    showToast("已加入购物车");
}

async function updateCart(productId, quantity) {
    state.cart = await api(`/api/cart/items/${productId}`, {
        method: "PATCH",
        body: JSON.stringify({ quantity })
    });
    renderCart();
}

document.addEventListener("click", async (event) => {
    const addButton = event.target.closest(".add-cart");
    const cartButton = event.target.closest("[data-cart-action]");
    const tabButton = event.target.closest(".tab");

    try {
        if (addButton) {
            await addToCart(addButton.dataset.productId);
            return;
        }

        if (cartButton) {
            const action = cartButton.dataset.cartAction;
            const productId = cartButton.dataset.productId;
            const quantity = Number(cartButton.dataset.quantity || 0);
            if (action === "increase") {
                await updateCart(productId, quantity + 1);
            } else if (action === "decrease") {
                await updateCart(productId, Math.max(0, quantity - 1));
            } else if (action === "remove") {
                state.cart = await api(`/api/cart/items/${productId}`, { method: "DELETE" });
                renderCart();
            }
            return;
        }

        if (tabButton) {
            document.querySelectorAll(".tab").forEach((button) => button.classList.remove("active"));
            tabButton.classList.add("active");
            state.category = tabButton.dataset.category;
            renderProducts();
        }
    } catch (error) {
        showToast(error.message);
    }
});

searchInput.addEventListener("input", (event) => {
    state.keyword = event.target.value;
    renderProducts();
});

clearCartBtn.addEventListener("click", async () => {
    try {
        state.cart = await api("/api/cart", { method: "DELETE" });
        renderCart();
        showToast("购物车已清空");
    } catch (error) {
        showToast(error.message);
    }
});

refreshOrdersBtn.addEventListener("click", async () => {
    try {
        state.orders = await api("/api/orders");
        renderOrders();
        showToast("订单已刷新");
    } catch (error) {
        showToast(error.message);
    }
});

checkoutForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(checkoutForm);
    const payload = {
        buyerName: formData.get("buyerName").trim(),
        phone: formData.get("phone").trim(),
        address: formData.get("address").trim(),
        remark: formData.get("remark").trim()
    };

    checkoutBtn.disabled = true;
    try {
        const order = await api("/api/orders", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        checkoutForm.reset();
        await loadAll();
        showToast(`下单成功：${order.orderNo}`);
    } catch (error) {
        showToast(error.message);
        renderCart();
    }
});

loadAll().catch((error) => showToast(error.message));
