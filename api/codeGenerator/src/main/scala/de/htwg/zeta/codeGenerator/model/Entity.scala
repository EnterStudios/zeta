package de.htwg.zeta.codeGenerator.model

/**
 * Representing Entity in Concept
 *
 * @param name      name of the Entity.
 * @param fixValues all fix values
 * @param inValues  all in values
 * @param outValues all out values
 * @param links     all Link Connections
 * @param maps      all Map Connections
 * @param refs      all Reference Connections
 */
case class Entity(
    name: String,
    fixValues: List[Value],
    inValues: List[Value],
    outValues: List[Value],
    links: List[Link],
    maps: List[MapLink],
    refs: List[ReferenceLink]
)
