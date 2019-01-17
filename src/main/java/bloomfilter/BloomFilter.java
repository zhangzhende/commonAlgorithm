package bloomfilter;

import java.util.BitSet;

/**
 *
 * 布隆过滤器-简单实现原理
 * 说明:通过将字符串用k钟散列方式，布隆过滤器需要的是一个位数组（这个和位图有点类似）和k个映射函数（和Hash表类似），
 * 在初始状态时，对于长度为m的位数组array，它的所有位都被置为0。对于有n个元素的集合S={s1,s2......sn}，
 * 通过k个映射函数{f1,f2,......fk}，将集合S中的每个元素sj(1<=j<=n)映射为k个值{g1,g2......gk}，
 * 然后再将位数组array中相对应的array[g1],array[g2]......array[gk]置为1；如果要查找某个元素item是否在S中，
 * 则通过映射函数{f1,f2.....fk}得到k个值{g1,g2.....gk}，然后再判断array[g1],array[g2]......array[gk]是否都为1，
 * 若全为1，则item在S中，否则item不在S中。这个就是布隆过滤器的实现原理。
 *
 *
 */
public class BloomFilter {

    private static final int BIT_SIZE = 2 << 28;//二进制向量的位数，相当于能存储1000万条url左右，误报率为千万分之一
    private static final int[] seeds = new int[]{3, 5, 7, 11, 13, 31, 37, 61};//用于生成信息指纹的8个随机数，最好选取质数

    private BitSet bits = new BitSet(BIT_SIZE);
    private Hash[] func = new Hash[seeds.length];//用于存储8个随机哈希值对象

    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new Hash(BIT_SIZE, seeds[i]);
        }
    }

    /**
     * 像过滤器中添加字符串
     */
    public void addValue(String value) {
        //将字符串value哈希为8个或多个整数，然后在这些整数的bit上变为1
        if (value != null) {
            for (Hash f : func)
                bits.set(f.hash(value), true);
        }

    }

    /**
     * 判断字符串是否包含在布隆过滤器中
     */
    public boolean contains(String value) {
        if (value == null)
            return false;

        boolean ret = true;

        //将要比较的字符串重新以上述方法计算hash值，再与布隆过滤器比对
        for (Hash f : func)
            ret = ret && bits.get(f.hash(value));
        return ret;
    }

    /**
     * 随机哈希值对象
     */

    public static class Hash {
        private int size;//二进制向量数组大小
        private int seed;//随机数种子

        public Hash(int cap, int seed) {
            this.size = cap;
            this.seed = seed;
        }

        /**
         * 计算哈希值(也可以选用别的恰当的哈希函数)
         */
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }

            return (size - 1) & result;
        }
    }
    public static void main(String[] args){

        BloomFilter b = new BloomFilter();
        b.addValue("www.baidu.com");
        b.addValue("www.sohu.com");

        System.out.println(b.contains("www.baidu.com"));
        System.out.println(b.contains("www.sina.com"));
    }
}
