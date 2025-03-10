package com.orehorez.poe_agent.repository;

import com.orehorez.poe_agent.Classes.Currency;
import com.orehorez.poe_agent.Classes.CurrencyDate;
import com.orehorez.poe_agent.Classes.SampleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public interface CurrencyDateRepository extends JpaRepository<CurrencyDate, Integer> {

    Optional<CurrencyDate> findCurrencyDateByCurrencyAndSampleDate(Currency currency, SampleDate sampleDate);


//    CurrencyDate findFirstByCurrencyOrderBySampleDateDesc(Currency currency);
    Optional<CurrencyDate> findFirstByCurrencyOrderBySampleDateDesc(Currency currency);
    CurrencyDate findFirstCurrencyDateByCurrencyOrderBySampleDateDesc(Currency currency);


    @Query("SELECT cd.payChaos FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    Double findLatestPayChaosByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT cd.receiveChaos FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    Double findLatestReceiveChaosByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT cd.paySparkLine FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    String findLatestPaySparkLineByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT cd.receiveSparkLine FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    String findLatestReceiveSparkLineByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT cd.payTotalChange FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    Double findLatestPayTotalChangeByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT cd.receiveTotalChange FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC LIMIT 1")
    Double findLatestReceiveTotalChangeByCurrency(@Param("currencyId") Long currencyId);

    @Query("SELECT CurrencyDate FROM CurrencyDate cd WHERE cd.currency.currencyId = :currencyId ORDER BY cd.sampleDate.dateId DESC ")
    List<CurrencyDate> findAllByCurrencyId(@Param("currencyId") Long currencyId);

    List<CurrencyDate> findAllByCurrencyCurrencyId(Long id);

}
