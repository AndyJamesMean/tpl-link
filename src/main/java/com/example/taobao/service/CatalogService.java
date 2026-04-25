package com.example.taobao.service;

import com.example.taobao.exception.BusinessException;
import com.example.taobao.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogService {

    private final Map<Long, Product> products = new LinkedHashMap<Long, Product>();

    @PostConstruct
    public void init() {
        add(new Product(1L, "Mate X6 折叠屏手机", "12GB+512GB，旗舰影像，顺丰包邮",
                "华南数码旗舰店", new BigDecimal("12999.00"), 12, 238, "限时直降", "phone"));
        add(new Product(2L, "轻奢通勤托特包", "头层牛皮，大容量分区，适合上班和短途旅行",
                "鹿屿原创女包", new BigDecimal("399.00"), 45, 1560, "爆款", "bag"));
        add(new Product(3L, "智能恒温电水壶", "1.7L 大容量，316不锈钢内胆，APP远程保温",
                "云起生活电器", new BigDecimal("169.00"), 60, 842, "满减", "kettle"));
        add(new Product(4L, "零压护颈记忆枕", "慢回弹支撑，A/B双曲线，送可拆洗枕套",
                "星眠家居旗舰店", new BigDecimal("129.00"), 30, 521, "包邮", "pillow"));
        add(new Product(5L, "机械键盘青轴 87键", "PBT键帽，RGB背光，热插拔轴座",
                "极客外设工坊", new BigDecimal("259.00"), 20, 709, "新品", "keyboard"));
        add(new Product(6L, "春季宽松连帽卫衣", "400g重磅棉，男女同款，多色可选",
                "日落街头服饰", new BigDecimal("188.00"), 80, 2241, "第二件半价", "hoodie"));
    }

    public synchronized List<Product> listProducts() {
        return new ArrayList<Product>(products.values());
    }

    public synchronized Product getProduct(Long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return product;
    }

    public synchronized void checkAvailable(Map<Long, Integer> quantities) {
        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            Product product = getProduct(entry.getKey());
            int quantity = entry.getValue();
            if (quantity <= 0) {
                throw new BusinessException("购买数量必须大于0");
            }
            if (product.getStock() < quantity) {
                throw new BusinessException(product.getTitle() + "库存不足，当前仅剩" + product.getStock() + "件");
            }
        }
    }

    public synchronized void decreaseStocks(Map<Long, Integer> quantities) {
        checkAvailable(quantities);
        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            Product product = getProduct(entry.getKey());
            int quantity = entry.getValue();
            product.setStock(product.getStock() - quantity);
            product.setSoldCount(product.getSoldCount() + quantity);
        }
    }

    private void add(Product product) {
        products.put(product.getId(), product);
    }
}
