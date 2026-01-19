package com.qtglobal.practicaltest.util


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HashUtilTest {

    @Test
    fun computeSHA256_withValidString_returnsCorrectHash() {
        // Arrange
        val input = "hello"
        val expectedHash = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }

    @Test
    fun computeSHA256_withEmptyString_returnsCorrectHash() {
        // Arrange
        val input = ""
        val expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }

    @Test
    fun computeSHA256_withLongString_returnsCorrectHash() {
        // Arrange
        val input = "Qt global is a technology company"
        val expectedHash = "9f927bd1346d8058dbc4e31657a79fab19932b1a8b789a5d6189556f59635637"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }

    @Test
    fun computeSHA256_withByteArray_returnsCorrectHash() {
        // Arrange
        val input = byteArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F) // "Hello" in bytes
        val expectedHash = "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }

    @Test
    fun computeSHA256_withEmptyByteArray_returnsCorrectHash() {
        // Arrange
        val input = ByteArray(0)
        val expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }

    @Test
    fun verifyHash_withMatchingHashes_returnsTrue() {
        // Arrange
        val hash1 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val hash2 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"

        // Act
        val result = HashUtil.verifyHash(hash1, hash2)

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun verifyHash_withNonMatchingHashes_returnsFalse() {
        // Arrange
        val hash1 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val hash2 = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"

        // Act
        val result = HashUtil.verifyHash(hash1, hash2)

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun verifyHash_withCaseInsensitiveMatching_returnsTrue() {
        // Arrange
        val hash1 = "2CF24DBA5FB0A30E26E83B2AC5B9E29E1B161E5C1FA7425E73043362938B9824"
        val hash2 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"

        // Act
        val result = HashUtil.verifyHash(hash1, hash2)

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun verifyHash_withEmptyHashes_returnsTrue() {
        // Arrange
        val hash1 = ""
        val hash2 = ""

        // Act
        val result = HashUtil.verifyHash(hash1, hash2)

        // Assert
        assertThat(result).isTrue()
    }


    @Test
    fun computeSHA256_withSpecialCharacters_returnsCorrectHash() {
        // Arrange
        val input = "Hello, World! 123 @#$%"
        val expectedHash = "989bef167b5bfd470fb5697bc53b89be3fa3684da97371fb7e64404fdd50898d"

        // Act
        val result = HashUtil.computeSHA256(input)

        // Assert
        assertThat(result).isEqualTo(expectedHash)
    }


}
