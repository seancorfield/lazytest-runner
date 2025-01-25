# lazytest/test-runner

Temporary library to show how to write `TestRunner` implementations for
the Cognitect `test-runner` for PR [#49](https://github.com/cognitect-labs/test-runner/pull/49).

## Usage

Example uses:

Run just LazyTest tests (via `-main` which sets `--test-runner-fn`):

```
clojure -M:test -m lazytest.test-runner
```

Run just LazyTest tests via `test` API:

```
clojure -X:test lazytest.test-runner/test
```

Run both LazyTest and `clojure.test` tests via `test+` API:

```
clojure -X:test lazytest.test-runner/test+
```

Run just `clojure.test` tests via the Cognitect `test` API:

```
clojure -X:test cognitect.test-runner.api/test
```

Run both types of tests and use `dots` output for LazyTest:

```
clojure -X:test lazytest.test-runner/test+ :outputs '[dots]'
```

Run both types of tests and use `junit` output for `clojure.test`:

```
clojure -X:test lazytest.test-runner/test+ :outputs '[junit]'
```

Run both types of tests and use `junit` output for `clojure.test` and `dots` for LazyTest:

```
clojure -X:test lazytest.test-runner/test+ :outputs '[junit dots]'
```

## License

Copyright © 2025 Sean Corfield

Distributed under the Eclipse Public License version 1.0.
