SELECT cd.pay_chaos, cd.receive_chaos, c.`name`, c.currency_id, sd.`date`, sd.date_id, sd.league_id FROM currency_date cd
LEFT JOIN currency c ON c.currency_id=cd.currency_currency_id
LEFT JOIN sample_date sd ON sd.date_id=cd.sample_date_date_id