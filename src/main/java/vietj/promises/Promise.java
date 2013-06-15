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
package vietj.promises;

import com.google.common.base.Function;

import java.util.concurrent.Callable;

/**
 * A promise.
 *
 * @param <V> the value
 * @author Julien Viet
 */
public abstract class Promise<V> {

  public static <V> Promise<V> wrap(V value) {
    final Deferred<V> deferred = new Deferred<V>();
    deferred.resolve(value);
    return new Promise<V>() {
      @Override
      public <R> Promise<R> then(PromisingCallback<V, R> onFulfilled, PromisingCallback<Exception, R> onRejected) {
        return deferred.promise.then(onFulfilled, onRejected);
      }
    };
  }

  public abstract <R> Promise<R> then(PromisingCallback<V, R> onFulfilled, PromisingCallback<Exception, R> onRejected);

  public <R> Promise<R> then(PromisingCallback<V, R> onFulfilled) {
    return then(onFulfilled, new PromisingCallback<Exception, R>() {
      public Promise<R> call(Exception arg) throws Exception {
        throw arg;
      }
    });
  }

  private static class CallbackAdapter<V, R> implements PromisingCallback<V, R> {
    private final Callback<V, R> callback;
    private CallbackAdapter(Callback<V, R> callback) {
      this.callback = callback;
    }
    public Promise<R> call(V arg) throws Exception {
      return wrap(callback.call(arg));
    }
  }

  public <R> Promise<R> then(Callback<V, R> onFulfilled, Callback<Exception, R> onRejected) {
    return then(new CallbackAdapter<V, R>(onFulfilled), new CallbackAdapter<Exception, R>(onRejected));
  }

  public <R> Promise<R> then(Callback<V, R> onFulfilled) {
    return then(onFulfilled, new Callback<Exception, R>() {
      public R call(Exception arg) throws Exception {
        throw arg;
      }
    });
  }

  private static class FunctionAdapter<V, R> implements PromisingCallback<V, R> {
    private final Function<V, R> handler ;
    private FunctionAdapter(Function<V, R> handler) {
      this.handler = handler;
    }
    public Promise<R> call(V arg) throws Exception {
      return wrap(handler.apply(arg));
    }
  }

  public <R> Promise<R> then(Function<V, R> onFulfilled, Function<Exception, R> onRejected) {
    return then(new FunctionAdapter<V, R>(onFulfilled), new FunctionAdapter<Exception, R>(onRejected));
  }

  public <R> Promise<R> then(Function<V, R> onFulfilled) {
    return then(new FunctionAdapter<V, R>(onFulfilled));
  }

  private static class CallableAdapter<V, R> implements PromisingCallback<V, R> {
    private final Callable<R> handler ;
    private CallableAdapter(Callable<R> handler) {
      this.handler = handler;
    }
    public Promise<R> call(V arg) throws Exception {
      return wrap(handler.call());
    }
  }

  public <R> Promise<R> then(Callable<R> onFulfilled, Callable<R> onRejected) {
    return then(new CallableAdapter<V, R>(onFulfilled), new CallableAdapter<Exception, R>(onRejected));
  }

  public <R> Promise<R> then(Callable<R> onFulfilled) {
    return then(new CallableAdapter<V, R>(onFulfilled));
  }

  private static class RunnableAdapter<V, R> implements PromisingCallback<V, R> {
    private final Runnable handler ;
    private RunnableAdapter(Runnable handler) {
      this.handler = handler;
    }
    public Promise<R> call(V arg) throws Exception {
      handler.run();
      return wrap(null);
    }
  }

  public <R> Promise<R> then(Runnable onFulfilled, Runnable onRejected) {
    return then(new RunnableAdapter<V, R>(onFulfilled), new RunnableAdapter<Exception, R>(onRejected));
  }

  public <R> Promise<R> then(Runnable onFulfilled) {
    return then(new RunnableAdapter<V, R>(onFulfilled));
  }
}
