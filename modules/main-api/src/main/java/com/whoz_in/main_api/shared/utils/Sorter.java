package com.whoz_in.main_api.shared.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// 타입 T에 대해 정렬을 수행해주는 클래스
// Builder 를 만들고, 사용할 Comparator 를 세팅한 후, sort 를 수행한다.
public class Sorter<T> {

    private final List<Comparator<T>> comparators;

    private Sorter(){
        comparators = new ArrayList<>();
    }

    private Sorter(List<Comparator<T>> comparators){
        this.comparators = comparators;
    }

    public List<T> sort(List<T> list){
        Optional<Comparator<T>> comparator = comparators.stream()
                .reduce(Comparator::thenComparing);

        comparator.ifPresent(list::sort);

        return list;
    }

    public static <T> Sorter.Builder<T> builder(){
        return new Builder<>();
    }

    // 타입 T를 정렬하는 Sorter 를 만들어주는 클래스
    public static class Builder<T> {

        private final List<Comparator<T>> comparators;

        public Builder(){
            this.comparators = new ArrayList<>();
        }

        public Builder<T> comparator(Comparator<T> comparator){
            this.comparators.add(comparator);
            return this;
        }

        public Sorter<T> build(){
            return new Sorter<>(comparators);
        }

    }
}
