# 淘购下单台

一个使用 Spring Boot 2.7 + Maven 实现的电商购物下单演示项目，包含后端 REST API 和前端静态页面。

## 功能

- 商品列表和分类筛选
- 商品搜索
- 加入购物车、修改数量、删除商品、清空购物车
- 填写收货信息并提交订单
- 自动扣减库存
- 查看最近订单

## 运行

```bash
mvn spring-boot:run
```

浏览器访问：

```text
http://localhost:8080
```

## API

- `GET /api/products`：商品列表
- `GET /api/cart`：查看购物车
- `POST /api/cart/items`：加入购物车
- `PATCH /api/cart/items/{productId}`：修改购物车数量
- `DELETE /api/cart/items/{productId}`：删除购物车商品
- `DELETE /api/cart`：清空购物车
- `POST /api/orders`：提交订单
- `GET /api/orders`：订单列表

当前项目使用内存数据存储，重启应用后购物车和订单会清空。
