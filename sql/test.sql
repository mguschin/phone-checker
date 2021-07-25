select count(*) as RESULT
  from (
               (select 1 as RESULT from TABLE1 where PHONE = '79876543210' limit 1)
               union all
               (select 1 from TABLE2 where PHONE = '9876543210' limit 1)
       )as t