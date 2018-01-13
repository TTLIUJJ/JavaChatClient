package com.cherry.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {
    private volatile JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379);

    public void set(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            jedis.set(key, value);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean exists(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.exists(key);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return  false;
    }

    public long del(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.del(key);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return -1;
    }


    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.sadd(key, value);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return -1;
    }

    public long srem(String key, String ...member){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.srem(key, member);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return -1;
    }

    public Set<String> smembers(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.smembers(key);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public boolean sismember(String key, String member){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.sismember(key, member);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public String hmset(String key, Map<String, String> hash){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.hmset(key, hash);
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    public String hmget(String key, String field){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            List<String> strings = jedis.hmget(key, field);
            return strings.remove(0);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long hdel(String key, String ... field){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.hdel(key, field);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return -1;
    }

    public long rpush(String key, String ... field){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.rpush(key, field);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return -1;
    }

    public String lpop(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.lpop(key);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public String lindex(String key, int index){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.lindex(key, index);
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }
}
