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

import java.util.concurrent.atomic.AtomicReference;

/** @author Julien Viet */
public class Deferred<V> {

  /** . */
  private final AtomicReference<Status<V>> current = new AtomicReference<Status<V>>(null);

  private static class Status<V> {

    /**
     * A pending status.
     *
     * @param <V>
     */
    static class Pending<V, R> extends Status<V> {
      final Deferred<R> deferred;
      final PromisingCallback<V, Void> resolve;
      final PromisingCallback<Exception, Void> reject;
      Pending(final PromisingCallback<V, R> onFulfilled, final PromisingCallback<Exception, R> onRejected, final Pending<V, ?> previous) {
        PromisingCallback<V, Void> resolve = new PromisingCallback<V, Void>() {
          public Promise<Void> call(V arg) throws Exception {
            if (previous != null) {
              previous.resolve.call(arg);
            }
            try {
              Promise<R> result = onFulfilled.call(arg);
              deferred.resolve(result);
            }
            catch (Exception e) {
              deferred.reject(e);
            }
            return null;
          }
        };
        PromisingCallback<Exception, Void> reject = new PromisingCallback<Exception, Void>() {
          public Promise<Void> call(Exception arg) throws Exception {
            if (previous != null) {
              previous.reject.call(arg);
            }
            try {
              Promise<R> result = onRejected.call(arg);
              deferred.resolve(result);
            }
            catch (Exception e) {
              deferred.reject(e);
            }
            return null;
          }
        };
        this.deferred = new Deferred<R>();
        this.resolve = resolve;
        this.reject = reject;
      }
    }

    static class Resolved<V> extends Status<V> {
      final Promise<V> value;
      Resolved(Promise<V> value) {
        if (value == null) {
          throw new NullPointerException();
        }
        this.value = value;
      }
    }
  }

  public void resolve(final V value) {
    resolve(new Promise<V>() {
      public <Void> Promise<Void> then(PromisingCallback<V, Void> onFulfilled, PromisingCallback<Exception, Void> onRejected) {
        try {
          onFulfilled.call(value);
        }
        catch (Exception e) {
          // ??
        }
        return null;
      }
    });
  }

  public void reject(final Exception reason) {
    resolve(new Promise<V>() {
      public <Void> Promise<Void> then(PromisingCallback<V, Void> onFulfilled, PromisingCallback<Exception, Void> onRejected) {
        try {
          onRejected.call(reason);
        }
        catch (Exception e) {
          // ??
        }
        return null;
      }
    });
  }

  public void resolve(Promise<V> value) {
    while (true) {
      Status<V> status = current.get();
      if (status == null || status instanceof Status.Pending<?, ?>) {
        Status.Resolved<V> resolved = new Status.Resolved<V>(value);
        if (current.compareAndSet(status, resolved)) {
          Status.Pending<V, ?> pending = (Status.Pending<V, ?>)status;
          if (pending != null) {
            value.then(pending.resolve, pending.reject);
          }
        }
      } else {
        break;
      }
    }
  }

  public final Promise<V> promise = new Promise<V>() {
    public <R> Promise<R> then(PromisingCallback<V, R> onFulfilled, PromisingCallback<Exception, R> onRejected) {

      while (true) {
        Status<V> status = current.get();
        if (status == null || status instanceof Status.Pending<?, ?>) {
          Status.Pending<V, ?> previous = (Status.Pending<V, ?>)status;
          Status.Pending<V, R> next = new Status.Pending<V, R>(onFulfilled, onRejected, previous);
          if (current.compareAndSet(status, next)) {
            return next.deferred.promise;
          }
        } else if (status instanceof Status.Resolved<?>) {
          Status.Resolved<V> resolved = (Status.Resolved<V>)status;
          return resolved.value.then(onFulfilled, onRejected);
        } else {
          throw new UnsupportedOperationException("todo");
        }
      }
    }
  };
}
