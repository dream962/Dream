package com.util.structure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.util.ThreadSafeRandom;

/**
 * 二叉堆<br>
 * 二叉堆是一种完全二叉树，其任意子树的左右节点（如果有的话）的键值一定比根节点大(即：根节点最小).
 * 
 * @author dream
 * 
 */
public class BinaryHeap<T>
{
    private List<T> data;

    private Comparator<T> comparator;

    public BinaryHeap(Comparator<T> comparator, int size)
    {
        this.comparator = comparator;
        data = new ArrayList<>(size);
    }

    public BinaryHeap(Comparator<T> comparator)
    {
        this(comparator, 1);
    }

    public Comparator<T> getComparator()
    {
        return this.comparator;
    }

    public static void main(String[] args)
    {
        BinaryHeap<Integer> binary = new BinaryHeap<Integer>(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                return o1.intValue() - o2.intValue();
            }
        });
        for (int i = 0; i < 10; i++)
        {
            binary.push(ThreadSafeRandom.next(100));
        }

        System.err.println(binary.toString());
        System.err.println(binary.toSortString());
    }

    /**
     * 添加元素
     * 
     * @param node
     *            欲添加的元素对象
     */
    public void push(T node)
    {
        // 将新节点添至末尾先
        data.add(node);
        int len = data.size();

        // 若数组中只有一个元素则省略排序过程，否则对新元素执行上浮过程
        if (len > 1)
        {
            /** 新添入节点当前所在索引+1 */
            int index = len;

            /** 取得新节点当前父节点所在索引 */
            int parentIndex = index / 2 - 1 < 0 ? 0 : index / 2 - 1;

            T temp;

            // 和它的父节点（位置为当前位置除以2取整，比如第4个元素的父节点位置是2，第7个元素的父节点位置是3）比较，
            // 如果新元素比父节点元素小则交换这两个元素，然后再和新位置的父节点比较，直到它的父节点不再比它大，
            // 或者已经到达顶端，及第1的位置

            while (compareTwoNodes(node, data.get(parentIndex)))
            {
                temp = data.get(parentIndex);
                data.set(parentIndex, node);
                data.set(index - 1, temp);
                index /= 2;// 父节点所在的位置+1
                parentIndex = index / 2 - 1 < 0 ? 0 : index / 2 - 1;// 父节点的父节点
            }
        }

    }

    /** 弹出列表中第一个元素 */
    public T pop()
    {
        if (data.size() <= 0)
            return null;

        // 先弹出列首元素
        T result = data.remove(0);

        /** 数组长度 */
        int len = data.size();

        // 若弹出列首元素后数组空了或者其中只有一个元素了则省略排序过程，否则对列尾元素执行下沉过程
        if (len > 1)
        {
            /** 列尾节点 */
            T lastNode = data.remove(len - 1);

            // 将列尾元素排至首位
            data.add(0, lastNode);

            /** 末尾节点当前所在索引 */
            int index = 0;

            /** 末尾节点当前第一子节点所在索引 */
            int childIndex = index * 2 + 1;

            /** 末尾节点当前两个子节点中较小的一个的索引 */
            int comparedIndex;

            T temp;

            // 和它的两个子节点比较，如果较小的子节点比它小就将它们交换，直到两个子节点都比它大
            while (childIndex < len)
            {
                // 只有一个子节点的情况
                if (childIndex + 1 == len)
                {
                    comparedIndex = childIndex;
                }
                // 有两个子节点则取其中较小的那个
                else
                {
                    comparedIndex = compareTwoNodes(data.get(childIndex), data.get(childIndex + 1)) ? childIndex : childIndex + 1;
                }

                if (compareTwoNodes(data.get(comparedIndex), lastNode))
                {
                    temp = data.get(comparedIndex);
                    data.set(comparedIndex, lastNode);
                    data.set(index, temp);
                    index = comparedIndex;
                    childIndex = index * 2 + 1;
                }
                else
                {
                    break;
                }
            }

        }

        return result;
    }

    public T peek()
    {
        if (data.size() > 0)
            return data.get(0);
        else
            return null;
    }

    public T get(int i)
    {
        if (i >= data.size())
            return null;

        return data.get(i);
    }

    /**
     * 更新某一个节点的值。在你改变了二叉堆中某一节点的值以后二叉堆不会自动进行排序，所以你需要手动
     * 调用此方法进行二叉树更新
     */
    public void updateNode(T node)
    {
        int index = data.indexOf(node) + 1;
        int parentIndex = (index / 2 - 1) < 0 ? 0 : index / 2 - 1;

        T temp;
        // 上浮过程
        while (compareTwoNodes(node, data.get(parentIndex)))
        {
            temp = data.get(parentIndex);
            data.set(parentIndex, node);
            data.set(index - 1, temp);
            index /= 2;
            parentIndex = (index / 2 - 1) < 0 ? 0 : index / 2 - 1;
        }
    }

    /** 查找某节点所在索引位置 */
    public int indexOf(T node)
    {
        int index = data.indexOf(node);
        return index;
    }

    public int getSize()
    {
        return data.size();
    }

    public boolean contains(T t)
    {
        if (data.contains(t))
            return true;

        return false;
    }

    public void clear()
    {
        data.clear();
    }

    /** 比较两个节点，返回true则表示第一个节点小于第二个 */
    private boolean compareTwoNodes(T node1, T node2)
    {
        if (comparator != null)
        {
            return this.comparator.compare(node1, node2) < 0;
        }
        else
        {
            if (node1 instanceof Comparator)
            {
                @SuppressWarnings("unchecked")
                Comparator<T> comparator = (Comparator<T>) node1;
                if (comparator != null)
                {
                    return comparator.compare(node1, node2) < 0;
                }
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("BinaryHeap:{");
        for (int i = 0; i < data.size(); i++)
            result.append("[" + i + "]:" + data.get(i).toString() + " , ");
        return result.append("}").toString();
    }

    public String toSortString()
    {
        StringBuilder result = new StringBuilder();
        result.append("BinaryHeap:[");

        T t = pop();
        while (t != null)
        {
            result.append(t.toString()).append(",");
            t = pop();
        }

        return result.append("]").toString();
    }
}
