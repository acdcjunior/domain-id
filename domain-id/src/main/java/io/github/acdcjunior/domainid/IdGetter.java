/*
 * Copyright (c) 2019 domain-id authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.acdcjunior.domainid;


/**
 * Interface used to "convert" an instance of an object into its ID.
 * @param <E> the entity class
 * @param <ID> the type of the entity class' ID
 */
public interface IdGetter<E, ID extends DomainId> {

    /**
     * Gets the ID from a given entity class instance.
     *
     * @param entity the entity class instance.
     * @return the id of the given instance.
     */
    ID getId(E entity);

}
