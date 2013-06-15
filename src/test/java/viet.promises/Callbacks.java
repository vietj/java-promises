/*
 * Copyright 2013 Julien Viet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package viet.promises;

import vietj.promises.Callback;

import java.util.Collection;

/** @author Julien Viet */
public class Callbacks {

  public static <T> Callback<T, T> id() {
    return new Callback<T, T>() {
      public T call(T arg) throws Exception {
        return arg;
      }
    };
  }

  public static <T> Callback<T, Void> throwHandler(final Exception ex) {
    return new Callback<T, Void>() {
      public Void call(T arg) throws Exception {
        throw ex;
      }
    };
  }

  public static <T> Callback<T, Void> throwHandler() {
    return throwHandler(new Exception());
  }

  public static <T> Callback<T, Void> addToHandler(final Collection<T> collection) {
    return new Callback<T, Void>() {
      public Void call(T arg) throws Exception {
        collection.add(arg);
        return null;
      }
    };
  }

  public static <T, E> Callback<T, Void> addToHandler(final Collection<E> collection, final E element) {
    return new Callback<T, Void>() {
      public Void call(T arg) throws Exception {
        collection.add(element);
        return null;
      }
    };
  }

  public static <T> Callback<T, Void> nullHandler() {
    return new Callback<T, Void>() {
      public Void call(T arg) throws Exception {
        return null;
      }
    };
  }

  public static <T> Callback<T, String> toStringHandler() {
    return new Callback<T, String>() {
      public String call(T arg) throws Exception {
        return arg != null ? arg.toString() : null;
      }
    };
  }

}
