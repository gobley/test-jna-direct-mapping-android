#[unsafe(no_mangle)]
pub extern "C" fn add(lhs: i32, rhs: i32) -> i32 {
    lhs + rhs
}