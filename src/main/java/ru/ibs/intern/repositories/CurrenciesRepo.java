package ru.ibs.intern.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ibs.intern.entity.dictionaries.Currency;

@Repository
public interface CurrenciesRepo extends CrudRepository<Currency, Long> {

    Currency findByCurrencyCode(String code);

}
