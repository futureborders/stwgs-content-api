// Copyright 2021 Crown Copyright (Single Trade Window)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package uk.gov.cabinetoffice.bpdg.stwgs.cms.exception;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(final String resourceName, final String resourceId) {
    super(format("Resource '%s' not found with id '%s'", resourceName, resourceId));
  }

  public ResourceNotFoundException(
      final String resourceName, final String code, final String system, final String tradeType) {
    super(
        format(
            "Resource '%s' not found with trade type '%s', system '%s' and code '%s'",
            resourceName, tradeType, system, code));
  }

  public ResourceNotFoundException(
      final String resourceName, final String code, final String system) {
    super(
        format(
            "Resource '%s' not found with system '%s' and code '%s'", resourceName, system, code));
  }
}
