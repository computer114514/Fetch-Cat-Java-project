local rareCatId=ARGV[1];
local userId=ARGV[2];

local stockKey="rareCat:stock:"..rareCatId;
local rareCatBuyersKey="rareCat:buyer:"..rareCatId;
-- 传入的东西

-- 判断库存
local stock=redis.call("get",stockKey);
if not stock then
    return 1;
end
if(tonumber(stock)<=0) then
    return 1;
end
-- 判断用户
if(redis.call("sismember",rareCatBuyersKey,userId)==1) then
    return 2;
end
-- 库存减一
redis.call("incrby",stockKey,-1);
--用户+
redis.call("sadd",rareCatBuyersKey,userId);

return 0;