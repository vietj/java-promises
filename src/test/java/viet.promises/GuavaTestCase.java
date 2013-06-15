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

import com.google.common.base.Function;
import org.junit.Test;
import vietj.promises.Deferred;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

/** @author Julien Viet */
public class GuavaTestCase{

  @Test
  public void testFunction() {
    Deferred<Integer> deferred = new Deferred<Integer>();
    LinkedList<String> done = new LinkedList<String>();
    deferred.promise.then(new Function<Integer, String>() {
      public String apply(java.lang.Integer input) {
        return input.toString();
      }
    }).then(Callbacks.addToHandler(done));
    deferred.resolve(3);
    assertEquals(Arrays.asList("3"), done);
  }
}
