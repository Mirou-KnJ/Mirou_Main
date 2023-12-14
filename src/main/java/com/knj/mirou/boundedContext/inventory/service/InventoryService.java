package com.knj.mirou.boundedContext.inventory.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.inventory.model.entity.Inventory;
import com.knj.mirou.boundedContext.inventory.model.enums.InventoryStatus;
import com.knj.mirou.boundedContext.inventory.repository.InventoryRepository;
import com.knj.mirou.boundedContext.member.model.dtos.ProductReportDTO;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.service.ProductInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final ProductInfoService productInfoService;

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void create(Product product, Member member) {

        LocalDate expDate = LocalDate.now().plusDays(90);

        Inventory inventory = Inventory.builder()
                .owner(member)
                .product(product)
                .expDate(expDate)
                .status(InventoryStatus.BEFORE_USED)
                .build();

        inventoryRepository.save(inventory);
    }

    @Transactional
    public RsData<Long> usingProduct(long inventoryId, Member member) {

        Optional<Inventory> OInventory = getByIdAndOwner(inventoryId, member);
        if(OInventory.isEmpty()) {
            return RsData.of("F-1", "보관 정보를 확인할 수 없습니다.");
        }

        Inventory inventory = OInventory.get();
        inventory.usingProduct();

        return RsData.of("S-1", "사용이 완료되었습니다.");
    }

    public Optional<Inventory> getByIdAndOwner(long inventoryId, Member owner) {

        return inventoryRepository.findByIdAndOwner(inventoryId, owner);
    }

    public ProductReportDTO getProductReportDto() {

        ProductReportDTO reportDTO = new ProductReportDTO();

        List<Inventory> weeklySalesHistories = getWeeklySalesHistories();
        Map<Long, Integer> weeklySalesMap = getWeeklySalesMap(weeklySalesHistories);

        ProductInfo weeklyBestProduct = productInfoService.getById(weeklySalesMap.get(0L)).get();

        //FIXME
        reportDTO.setWeeklyBestProduct(weeklyBestProduct);
        reportDTO.setWeeklyBestProductSalesCount(weeklySalesMap.get(-1L));
        reportDTO.setWeeklyAllSalesCount(weeklySalesHistories.size());
        reportDTO.setWeeklyAllUsedCount(weeklySalesMap.get(-2L));

        return reportDTO;
    }

    public List<Inventory> getWeeklySalesHistories() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDayOfWeek = now.minus(6,
                ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);

        List<Inventory> weeklySalesHistories =
                inventoryRepository.findAllByCreateDateBetween(startDayOfWeek, now);

        return weeklySalesHistories;
    }

    public Map<Long, Integer> getWeeklySalesMap(List<Inventory> weeklySalesHistories) {

        Map<Long, Integer> weeklySalesMap = new HashMap<>();

        long bestProductId = -1;
        int bestProductCount = 0;
        int weeklyUsedCount = 0;

        for(Inventory history : weeklySalesHistories) {

            long infoId = history.getProduct().getInfo().getId();
            InventoryStatus status = history.getStatus();

            weeklySalesMap.compute(infoId, (key, value) -> (value == null) ? 1 : value + 1);

            int count = weeklySalesMap.get(infoId);

            if(count > bestProductCount) {
                bestProductId = infoId;
                bestProductCount = count;
            }

            //FIXME
            if(status.equals(InventoryStatus.AFTER_USED)) {
                weeklyUsedCount++;
            }
        }

        weeklySalesMap.put(0L, (int) bestProductId);
        weeklySalesMap.put(-1L, bestProductCount);
        weeklySalesMap.put(-2L, weeklyUsedCount);

        return weeklySalesMap;
    }
}
