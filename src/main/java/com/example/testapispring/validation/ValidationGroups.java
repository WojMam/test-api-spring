package com.example.testapispring.validation;

/**
 * Grupy walidacji do stosowania w różnych operacjach CRUD.
 */
public interface ValidationGroups {
    
    /**
     * Grupa walidacji używana podczas tworzenia nowego rekordu.
     */
    interface Create {}
    
    /**
     * Grupa walidacji używana podczas aktualizacji istniejącego rekordu.
     */
    interface Update {}
} 