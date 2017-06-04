package de.htwg.zeta.persistence.transient

import de.htwg.zeta.persistence.general.Repository
import models.User
import models.document.BondedTask
import models.document.EventDrivenTask
import models.document.Filter
import models.document.FilterImage
import models.document.Generator
import models.document.GeneratorImage
import models.document.Log
import models.document.MetaModelEntity
import models.document.MetaModelRelease
import models.document.ModelEntity
import models.document.Settings
import models.document.TimedTask


/** Cache-Implementation of the PersistenceService. */
class TransientRepository extends Repository {

  /** Persistence for the [[models.document.EventDrivenTask]] */
  override val eventDrivenTask = new TransientPersistence[EventDrivenTask]

  /** Persistence for the [[models.document.BondedTask]] */
  override val bondTask = new TransientPersistence[BondedTask]

  /** Persistence for [[models.document.TimedTask]] */
  override val timedTask = new TransientPersistence[TimedTask]

  /** Persistence for the [[models.document.Generator]] */
  override val generator = new TransientPersistence[Generator]

  /** Persistence for the [[models.document.Filter]] */
  override val filter = new TransientPersistence[Filter]

  /** Persistence for the [[models.document.GeneratorImage]] */
  override val generatorImage = new TransientPersistence[GeneratorImage]

  /** Persistence for the [[models.document.FilterImage]] */
  override val filterImage = new TransientPersistence[FilterImage]

  /** Persistence for the [[models.document.Settings]] */
  override val settings = new TransientPersistence[Settings]

  /** Persistence for the [[models.document.MetaModelEntity]] */
  override val metaModelEntity = new TransientPersistence[MetaModelEntity]

  /** Persistence for the [[models.document.MetaModelRelease]] */
  override val metaModelRelease = new TransientPersistence[MetaModelRelease]

  /** Persistence for the [[models.document.ModelEntity]] */
  override val modelEntity = new TransientPersistence[ModelEntity]

  /** Persistence for the [[models.document.Log]] */
  override val log = new TransientPersistence[Log]

  /** Persistence for the [[models.User]] */
  override val users = new TransientPersistence[User]

}
